import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter name for player 1: ");
        String player1Name = scanner.nextLine();
        System.out.print("Enter name for player 2: ");
        String player2Name = scanner.nextLine();

        Player player1 = new Player(player1Name, Player.BLACK);
        Player player2 = new Player(player2Name, Player.WHITE);

        // Initialize with a default board
        Game game = new Game(player1, player2);
        game.start();
    }
}
