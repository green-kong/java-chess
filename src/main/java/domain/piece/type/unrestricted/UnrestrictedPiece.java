package domain.piece.type.unrestricted;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import domain.board.Square;
import domain.piece.Camp;
import domain.piece.Piece;

public abstract class UnrestrictedPiece extends Piece {
    public UnrestrictedPiece(Camp camp) {
        super(camp);
    }

    @Override
    public List<Square> fetchMovePath(Square currentSquare, Square targetSquare) {
        List<Integer> gaps = calculateGap(currentSquare, targetSquare);
        Integer distance = calculateDistance(gaps);
        validateMovable(gaps);
        List<Integer> direction = calculateDirection(gaps, distance);
        return calculatePath(currentSquare, distance, direction);
    }

    private static List<Integer> calculateDirection(List<Integer> gaps, Integer distance) {
        return gaps.stream()
                .mapToInt(gap -> gap / distance)
                .boxed()
                .collect(Collectors.toList());
    }

    private List<Square> calculatePath(Square currentSquare, Integer distance, List<Integer> direction) {
        List<Integer> coordinate = currentSquare.toCoordinate();
        Integer currentFile = coordinate.get(FILE);
        Integer currentRank = coordinate.get(RANK);
        return IntStream.range(1, distance + 1)
                .mapToObj(dist -> new Square(currentFile + direction.get(FILE) * dist,
                        currentRank + direction.get(RANK) * dist))
                .collect(Collectors.toUnmodifiableList());
    }

    protected boolean isNotForward(List<Integer> gap) {
        Integer fileGap = gap.get(FILE);
        Integer RankGap = gap.get(RANK);
        return fileGap != 0 && RankGap != 0;
    }


    protected boolean isNotDiagonal(List<Integer> gap) {
        return !Objects.equals(Math.abs(gap.get(FILE)), Math.abs(gap.get(RANK)));
    }

    @Override
    public boolean canMove(Map<Square, Camp> pathInfo, Square targetSquare) {
        Camp targetCamp = pathInfo.get(targetSquare);
        pathInfo.remove(targetSquare);
        return isDifferentCampOrEmptyOnTarget(targetCamp) && !isExistPieceOnPath(pathInfo);
    }
}
