package domain.piece;

import java.util.List;
import java.util.Set;

public abstract class Piece {
    private final Camp camp;

    public Piece(Camp camp) {
        this.camp = camp;
    }

    protected static final int FILE = 0;
    protected static final int RANK = 1;

    public boolean isWhite() {
        return camp.equals(Camp.WHITE);
    }

    abstract public Set<List<Integer>> fetchMovableCoordinate(List<Integer> currentCoordinate);

    public boolean isPawn() {
        return false;
    }

    public boolean isRook() {
        return false;
    }

    public boolean isKnight() {
        return false;
    }

    public boolean isBishop() {
        return false;
    }

    public boolean isQueen() {
        return false;
    }

    public boolean isKing() {
        return false;
    }

}
