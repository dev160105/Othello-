public class Player {
    public static final char WHITE = 'W';
    public static final char BLACK = 'B';

    private String name;
    private char piece;

    public Player(String name, char piece) {
        this.name = name;
        this.piece = piece;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public char getPiece() {
        return piece;
    }
}
