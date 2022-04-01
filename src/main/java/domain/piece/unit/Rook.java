package domain.piece.unit;

import static domain.piece.property.Direction.*;

import domain.piece.property.PieceInfo;
import domain.piece.property.PieceFeature;
import domain.piece.property.Team;
import domain.piece.property.Direction;
import java.util.List;

public final class Rook extends CommonMovablePiece {

    private static final List<Direction> directions;

    static {
        directions = List.of(EAST, WEST, SOUTH, NORTH);
    }

    public Rook(final Team team) {
        super(new PieceInfo(team, PieceFeature.ROOK));
    }

    @Override
    public List<Direction> getDirections() {
        return directions;
    }
}
