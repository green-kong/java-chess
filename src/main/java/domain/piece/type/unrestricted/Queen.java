package domain.piece.type.unrestricted;

import java.util.List;

import domain.piece.Camp;
import domain.piece.type.PieceType;

public class Queen extends UnrestrictedPiece {
    public Queen(Camp camp) {
        super(camp, PieceType.QUEEN);
    }

    @Override
    protected void validateMovable(List<Integer> gaps) {
        if (isNotForward(gaps) && isNotDiagonal(gaps)) {
            throw new IllegalArgumentException("움직일 수 없는 경로입니다.");
        }
    }
}
