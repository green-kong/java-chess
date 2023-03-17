package domain.piece.type.restricted;

import java.util.List;
import java.util.Map;

import domain.board.Square;
import domain.piece.Camp;
import domain.piece.Piece;

public abstract class RestrictedPiece extends Piece {
    public RestrictedPiece(Camp camp) {
        super(camp);
    }

    @Override
    public List<Square> fetchMovePath(Square currentSquare, Square targetSquare) {
        List<Integer> gaps = calculateGap(currentSquare, targetSquare);
        validateMovable(gaps);
        return List.of(targetSquare);
    }

    @Override
    public boolean canMove(Map<Square, Camp> pathInfo, Square targetSquare) {
        Camp targetCamp = pathInfo.get(targetSquare);
        return isDifferentCampOrEmptyOnTarget(targetCamp);
    }
}
