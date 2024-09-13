import java.util.Scanner;

public class Game {
    private Player currentPlayer;
    private Board board;
    private Scanner scanner = new Scanner(System.in);
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2, int startPosition) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1; // Set initial player
        this.board = new Board(player1, player2, startPosition);
    }

    public Game(Player player1, Player player2) {
        this(player1, player2, 1); // Default to startPosition 1
    }

    public void start() {


        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Game!");
            System.out.println("1. Quit");
            System.out.println("2. Load a Game");
            System.out.println("3. Start a New Game");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    running = false;
                    quitGame();
                    break;
                case 2:
                    if (board != null) {
                        System.out.println("Please enter the filename:");
                        board.load(scanner.nextLine(), this);
                        play();
                    } else {
                        System.out.println("Board is not initialized.");
                    }
                    break;
                case 3:
                    if (player1 == null || player2 == null) {
                        System.out.println("Players are not initialized.");
                        return;
                    }

                    System.out.println("Choose the board (1 for offset board, 2 for regular board):");
                    board = new Board(player1, player2, scanner.nextInt());
                    scanner.nextLine(); // Consume the newline character
                    play();
                    break;
                default:
                    System.out.println("Invalid option. Please select a valid option.");
                    break;
            }
        }
    }

    private void quitGame() {
        System.out.println("Quitting the game...");
    }

    public void changePlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    private void saveGame() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter filename to save the game: ");
        String filename = sc.nextLine();
//        scanner.nextLine();
        board.save(filename);
        System.out.println("Game saved to " + filename);
    }

    private void play() {


        while (!board.isGameOver()) {
            int row=0, col=0;
            board.drawBoard();
            System.out.println("Player " + currentPlayer.getName() + "'s turn");
            System.out.println("Do you want to save, concede or move?\npress 1 to save\npress 2 to concede\npress 3 to make a move");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    saveGame();
                    break;
                case 2:
                    concede();
                    break;
                case 3:
                    System.out.print("Enter row: ");
                    row = scanner.nextInt();
                    System.out.print("Enter column: ");
                    col = scanner.nextInt();
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("You entered an invalid number. please try again.");
                    continue;
            }
            try {
                board.play(row, col, currentPlayer);

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try again.");
                continue;
            }

            changePlayer();
        }

        board.drawBoard();
        displayResults();
    }

    private void concede(){
        System.out.println(currentPlayer.getName()+" has conceded.");
        if (currentPlayer == player1) {
            System.out.println(player2.getName()+" wins!!");
        } else {
            System.out.println(player1.getName()+" wins!!");
        }
        System.exit(0);
    }


    private void displayResults() {
        int blackCount = 0, whiteCount = 0;
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                char piece = board.getPosition(i, j).getPiece();
                if (piece == Player.BLACK) {
                    blackCount++;
                } else if (piece == Player.WHITE) {
                    whiteCount++;
                }
            }
        }

        if (blackCount > whiteCount) {
            System.out.println("Black player wins!");
        } else if (whiteCount > blackCount) {
            System.out.println("White player wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }
}
