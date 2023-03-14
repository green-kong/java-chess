package domain.piece.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.piece.Camp;
import domain.piece.Direction;
import domain.piece.Piece;

public class King extends Piece {
    private static final List<Direction> movableDirection = Arrays.asList(Direction.values());

    public King(Camp camp) {
        super(camp);
    }

    @Override
    public boolean isKing() {
        return true;
    }

    @Override
    public Set<List<Integer>> fetchMovableCoordinate(List<Integer> currentCoordinate) {
        Integer file = currentCoordinate.get(FILE);
        Integer rank = currentCoordinate.get(RANK);

        Set<List<Integer>> movableCoordinate = new HashSet<>();

        for (Direction direction : movableDirection) {
            int fileCoordinate = file + direction.getFile();
            int rankCoordinate = rank + direction.getRank();

            if (fileCoordinate < 0 || fileCoordinate > 7 || rankCoordinate < 0 || rankCoordinate > 7) {
                continue;
            }

            movableCoordinate.add(List.of(fileCoordinate, rankCoordinate));
        }
        return movableCoordinate;
    }
}
