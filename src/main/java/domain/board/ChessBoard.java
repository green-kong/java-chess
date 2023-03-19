package domain.board;

import static domain.piece.Camp.BLACK;
import static domain.piece.Camp.WHITE;
import static domain.piece.type.Type.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import domain.piece.Camp;
import domain.piece.Piece;
import domain.piece.type.Empty;
import domain.piece.type.Pawn;
import domain.piece.type.restricted.King;
import domain.piece.type.restricted.Knight;
import domain.piece.type.unrestricted.Bishop;
import domain.piece.type.unrestricted.Queen;
import domain.piece.type.unrestricted.Rook;

public class ChessBoard {
    private final Map<Square, Piece> board = new HashMap<>();

    public ChessBoard() {
        for (File file : File.values()) {
            for (Rank rank : Rank.values()) {
                board.put(Square.of(file, rank), Empty.getInstance());
            }
        }
    }

    public void initialize() {
        Rank WHITE_WITHOUT_PAWN_RANK = Rank.ONE;
        Rank BLACK_WITHOUT_PAWN_RANK = Rank.EIGHT;
        Rank WHITE_PAWN_RANK = Rank.TWO;
        Rank BLACK_PAWN_RANK = Rank.SEVEN;

        initializePawns(WHITE_PAWN_RANK, BLACK_PAWN_RANK);
        initializeKings(WHITE_WITHOUT_PAWN_RANK, BLACK_WITHOUT_PAWN_RANK);
        initializeQueens(WHITE_WITHOUT_PAWN_RANK, BLACK_WITHOUT_PAWN_RANK);
        initializeBishops(WHITE_WITHOUT_PAWN_RANK, BLACK_WITHOUT_PAWN_RANK);
        initializeKnights(WHITE_WITHOUT_PAWN_RANK, BLACK_WITHOUT_PAWN_RANK);
        initializeRooks(WHITE_WITHOUT_PAWN_RANK, BLACK_WITHOUT_PAWN_RANK);
    }

    private void initializePawns(Rank WHITE_PAWN_RANK, Rank BLACK_PAWN_RANK) {
        List<Square> initialWhitePawnSquares = new ArrayList<>();
        List<Square> initialBlackPawnSquares = new ArrayList<>();

        for (File value : File.values()) {
            initialWhitePawnSquares.add(Square.of(value, WHITE_PAWN_RANK));
        }
        for (File value : File.values()) {
            initialBlackPawnSquares.add(Square.of(value, BLACK_PAWN_RANK));
        }
        for (Square initialWhitePawnSquare : initialWhitePawnSquares) {
            board.put(initialWhitePawnSquare, new Pawn(WHITE, PAWN));
        }
        for (Square initialBlackPawnSquare : initialBlackPawnSquares) {
            board.put(initialBlackPawnSquare, new Pawn(BLACK, PAWN));
        }
    }

    private void initializeKings(Rank WHITE_WITHOUT_PAWN_RANK, Rank BLACK_WITHOUT_PAWN_RANK) {
        board.put(Square.of(File.E, WHITE_WITHOUT_PAWN_RANK), new King(WHITE, KING));
        board.put(Square.of(File.E, BLACK_WITHOUT_PAWN_RANK), new King(BLACK, KING));
    }

    private void initializeQueens(Rank WHITE_WITHOUT_PAWN_RANK, Rank BLACK_WITHOUT_PAWN_RANK) {
        board.put(Square.of(File.D, WHITE_WITHOUT_PAWN_RANK), new Queen(WHITE, QUEEN));
        board.put(Square.of(File.D, BLACK_WITHOUT_PAWN_RANK), new Queen(BLACK, QUEEN));
    }

    private void initializeBishops(Rank WHITE_WITHOUT_PAWN_RANK, Rank BLACK_WITHOUT_PAWN_RANK) {
        List<File> initialBishopFiles = List.of(File.C, File.F);
        for (File initialBishopFile : initialBishopFiles) {
            Square square = Square.of(initialBishopFile, WHITE_WITHOUT_PAWN_RANK);
            board.put(square, new Bishop(WHITE, BISHOP));
        }
        for (File initialBishopFile : initialBishopFiles) {
            Square square = Square.of(initialBishopFile, BLACK_WITHOUT_PAWN_RANK);
            board.put(square, new Bishop(BLACK, BISHOP));
        }
    }

    private void initializeKnights(Rank WHITE_WITHOUT_PAWN_RANK, Rank BLACK_WITHOUT_PAWN_RANK) {
        List<File> initialKnightFiles = List.of(File.B, File.G);

        for (File initialKnightFile : initialKnightFiles) {
            Square square = Square.of(initialKnightFile, WHITE_WITHOUT_PAWN_RANK);
            board.put(square, new Knight(WHITE, KNIGHT));
        }

        for (File initialKnightFile : initialKnightFiles) {
            Square square = Square.of(initialKnightFile, BLACK_WITHOUT_PAWN_RANK);
            board.put(square, new Knight(BLACK, KNIGHT));
        }
    }

    private void initializeRooks(Rank WHITE_WITHOUT_PAWN_RANK, Rank BLACK_WITHOUT_PAWN_RANK) {
        List<File> initialRookFiles = List.of(File.A, File.H);

        for (File initialRookFile : initialRookFiles) {
            Square square = Square.of(initialRookFile, WHITE_WITHOUT_PAWN_RANK);
            board.put(square, new Rook(WHITE, ROOK));
        }

        for (File initialRookFile : initialRookFiles) {
            Square square = Square.of(initialRookFile, BLACK_WITHOUT_PAWN_RANK);
            board.put(square, new Rook(BLACK, ROOK));
        }
    }

    public Square toSquare(int fileCoordinate, int rankCoordinate) {
        File targetFile = File.findFile(fileCoordinate);
        Rank targetRank = Rank.findRank(rankCoordinate);
        return Square.of(targetFile, targetRank);
    }

    public void move(Square currentSquare, Square targetSquare) {
        Piece currentPiece = board.get(currentSquare);
        List<Square> path = currentPiece.fetchMovePath(currentSquare, targetSquare);
        Map<Square, Camp> pathInfo = calculatePathInfo(path);
        if (currentPiece.canMove(pathInfo, targetSquare)) {
            board.put(targetSquare, currentPiece);
            board.put(currentSquare, Empty.getInstance());
            return;
        }
        throw new IllegalStateException("움직일 수 없는 경로입니다.");
    }

    private Map<Square, Camp> calculatePathInfo(List<Square> path) {
        return path.stream()
                .collect(Collectors.toMap(Function.identity(), square -> board.get(square).getCamp()));
    }

    public Map<Square, Piece> getBoard() {
        return board;
    }

    public boolean isCorrectCamp(Camp currentCamp, Square currentSquare) {
        return currentCamp.equals(board.get(currentSquare).getCamp());
    }
}
