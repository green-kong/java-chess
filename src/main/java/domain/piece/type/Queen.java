package domain.piece.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.board.Square;
import domain.piece.Camp;
import domain.piece.Direction;
import domain.piece.Piece;

public class Queen extends Piece {
    private static final List<Direction> movableDirection = Arrays.asList(Direction.values());
    public Queen(Camp camp) {
        super(camp);
    }

    @Override
    public boolean isQueen() {
        return true;
    }

    @Override
    public List<Square> fetchMovableCoordinate(Square currentSquare, Square targetSquare) {
        List<Integer> currentCoordinate = currentSquare.toCoordinate();
        Integer file = currentCoordinate.get(FILE);
        Integer rank = currentCoordinate.get(RANK);

        List<Square> movableSquares = new ArrayList<>();

        for (Direction direction : movableDirection) {
            ArrayList<Square> squares = new ArrayList<>();
            for (int i = 1; i < 8; i++) {
                int fileCoordinate = file + (i * direction.getFile());
                int rankCoordinate = rank + (i * direction.getRank());
                if (isInCoordinateRange(fileCoordinate, rankCoordinate)) {
                    continue;
                }
                squares.add(new Square(fileCoordinate, rankCoordinate));
            }
            if (squares.contains(targetSquare)) {
                movableSquares = squares;
            }
        }
        return movableSquares;
    }
}
