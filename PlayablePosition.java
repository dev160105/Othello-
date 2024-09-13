public class PlayablePosition extends Position {
    public PlayablePosition(char piece) {
        this.piece = piece;
    }

    @Override
    public boolean canPlay() {
        return true;
    }
}
