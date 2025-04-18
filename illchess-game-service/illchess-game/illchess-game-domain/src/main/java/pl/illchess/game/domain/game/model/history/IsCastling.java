package pl.illchess.game.domain.game.model.history;

public record IsCastling(boolean value) {

    public static IsCastling yep() {
        return new IsCastling(true);
    }

    public static IsCastling nope() {
        return new IsCastling(false);
    }

}
