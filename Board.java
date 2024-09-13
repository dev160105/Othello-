import java.io.*;

public class Board {
    public static final int BOARD_SIZE = 8;

    private Position[][] board;
    private Player player1, player2;
    private Player currentPlayer;

    private static final char[][] START_POSITION_1 = {
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', 'W', 'W', 'B', 'B', '.', 'X'},
            {'.', '.', 'W', 'W', 'B', 'B', '.', 'X'},
            {'.', '.', 'B', 'B', 'W', 'W', '.', 'X'},
            {'.', '.', 'B', 'B', 'W', 'W', '.', 'X'},
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.'}
    };

    private static final char[][] START_POSITION_2 = {
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', 'X'},
            {'.', '.', '.', 'W', 'B', '.', '.', 'X'},
            {'.', '.', '.', 'B', 'W', '.', '.', 'X'},
            {'.', '.', '.', '.', '.', '.', '.', 'X'},
            {'.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.'}
    };

    public Board(Player p1, Player p2, int startPosition) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = p1;
        this.board = new Position[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(startPosition);
    }

    private void initializeBoard(int startPosition) {
        switch (startPosition) {
            case 1:
                initializeBoard(START_POSITION_1);
                break;
            case 2:
                initializeBoard(START_POSITION_2);
                break;
            default:
                throw new IllegalArgumentException("Invalid start position: " + startPosition);
        }
    }

    private void initializeBoard(char[][] startingPosition) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                char piece = startingPosition[i][j];
                if (piece == 'W') {
                    board[i][j] = new PlayablePosition(Player.WHITE);
                } else if (piece == 'B') {
                    board[i][j] = new PlayablePosition(Player.BLACK);
                } else {
                    board[i][j] = new UnplayablePosition();
                }
            }
        }
    }

    public void drawBoard() {
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(j == 7){
                    if(i == 2 || i == 3 || i == 4 || i == 5){
                        board[i][j].setPiece(UnplayablePosition.UNPLAYABLE);
                        char piece = board[i][j].getPiece();
                        System.out.print(piece + " ");
                    }
                }
                else {
                char piece = board[i][j].getPiece();
                    System.out.print(piece + " ");
                }

            }
            System.out.println();
        }
    }

    public void load(String filename, Game game) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String player1Name = reader.readLine();
            String player2Name = reader.readLine();
            String currentPlayerName = reader.readLine();

            Player player1 = new Player(player1Name, Player.WHITE);
            Player player2 = new Player(player2Name, Player.BLACK);
            Player currentPlayer = currentPlayerName.equals(player1Name) ? player1 : player2;

            this.board = new Position[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                String line = reader.readLine();
                for (int j = 0; j < BOARD_SIZE; j++) {
                    char c = line.charAt(j);
                    if (c == 'W') {
                        board[i][j] = new PlayablePosition(Player.WHITE);
                    } else if (c == 'B') {
                        board[i][j] = new PlayablePosition(Player.BLACK);
                    } else {
                        board[i][j] = new UnplayablePosition();
                    }
                }
            }

            game.setCurrentPlayer(currentPlayer);

        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
    }

    public void save(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(player1.getName());
            writer.newLine();
            writer.write(player2.getName());
            writer.newLine();
            writer.write(currentPlayer.getName());
            writer.newLine();

            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    writer.write(board[i][j].getPiece());
                }
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public void play(int row, int col, Player currentPlayer) {
        if (board == null || !canPlay(row, col, currentPlayer)) {
            throw new IllegalArgumentException("This is an invalid move!");
        }

        board[row][col].setPiece(currentPlayer.getPiece());
        flipDiscs(row, col, currentPlayer);
    }

    private void flipDiscs(int row, int col, Player currentPlayer) {
        char opponentPiece = (currentPlayer.getPiece() == Player.BLACK) ? Player.WHITE : Player.BLACK;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int r = row + dr;
                int c = col + dc;
                boolean foundOpponent = false;

                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c].getPiece() != Position.EMPTY) {
                    if (board[r][c].getPiece() == opponentPiece) {
                        foundOpponent = true;
                    } else if (board[r][c].getPiece() == currentPlayer.getPiece()) {
                        if (foundOpponent) {
                            r -= dr;
                            c -= dc;
                            while (r != row || c != col) {
                                board[r][c].setPiece(currentPlayer.getPiece());
                                r -= dr;
                                c -= dc;
                            }
                        }
                        break;
                    } else {
                        break;
                    }
                    r += dr;
                    c += dc;
                }
            }
        }
    }

    public boolean canPlay(int row, int col, Player player) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false; // Out of bounds
        }
        if (board[row][col].getPiece() != Position.EMPTY) {
            return false; // Position already occupied
        }

        // Check for at least one opponent piece in a valid direction
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue; // Skip the current position
                }
                int r = row + dr;
                int c = col + dc;
                boolean foundOpponent = false;
                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c].getPiece() != Position.EMPTY) {
                    if (board[r][c].getPiece() == (player.getPiece() == Player.BLACK ? Player.WHITE : Player.BLACK)) {
                        foundOpponent = true;
                    } else if (board[r][c].getPiece() == player.getPiece()) {
                        if (foundOpponent) {
                            return true; // Found a valid move
                        }
                        break;
                    } else {
                        break;
                    }
                    r += dr;
                    c += dc;
                }
            }
        }
        return false; // No valid move found
    }

    public boolean isGameOver() {
        return !hasValidMoves(player1) && !hasValidMoves(player2);
    }

    public boolean hasValidMoves(Player player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (canPlay(i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Position getPosition(int row, int col) {
        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            return board[row][col];
        }
        return null;
    }
}
