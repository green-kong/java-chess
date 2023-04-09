package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import domain.board.ChessBoard;
import domain.board.File;
import domain.board.Rank;
import domain.board.Square;
import domain.piece.Camp;
import domain.piece.Piece;
import domain.piece.factory.PieceFactory;
import dto.BoardDto;
import dto.GameInfoDto;
import dto.MoveHistoryDto;
import dto.ScoreDto;
import repository.connector.ProdConnector;
import repository.game.GameDao;
import repository.game.JdbcGameDao;

public class ChessService {
    private final ChessBoard chessBoard = new ChessBoard(new HashMap<>());

    public ChessService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    private final GameDao gameDao;
    private Camp currentCamp = Camp.WHITE;
    private boolean isOngoing;

    public void setUp(GameInfoDto gameInfoDto) {
        if (isOngoing) {
            throw new IllegalStateException("이미 게임이 실행중 입니다.");
        }
        chessBoard.initialize();
        for (BoardDto boardDto : gameInfoDto.getBoardDtos()) {
            chessBoard.setUpPieces(boardDto);
        }
        currentCamp = Camp.find(gameInfoDto.getCurrentTurn());
        isOngoing = true;
    }

    public void move(long roomId, String currentSquareInput, String targetSquareInput) {
        if (!isOngoing) {
            throw new IllegalStateException("start를 먼저 입력해주세요.");
        }
        Square currentSquare = getCurrentSquare(currentSquareInput);
        Square targetSquare = getTargetSquare(targetSquareInput);
        validateCurrentCamp(currentSquare);
        Piece pieceOnTarget = chessBoard.move(currentSquare, targetSquare);
        currentCamp = currentCamp.fetchOppositeCamp();
        isOngoing = !chessBoard.isCapturedKing(currentCamp);
        saveGameInfo(roomId, generateGameInfoDto());
        MoveHistoryDto moveHistoryDto = MoveHistoryDto.of(currentSquare, targetSquare, pieceOnTarget);
        gameDao.saveMoveHistory(roomId, moveHistoryDto);
    }

    public void cancelMove(long roomId) {
        List<MoveHistoryDto> lastTwoMoveHistories = gameDao.findLastTwoMoveHistories(roomId);
        for (MoveHistoryDto moveHistoryDto : lastTwoMoveHistories) {
            String parsedOrigin = moveHistoryDto.getSource();
            String parsedMoved = moveHistoryDto.getTarget();
            Piece piece = PieceFactory.find(moveHistoryDto.getPiece()).generatePiece(currentCamp);
            Square originSquare = Square.of(
                    File.findFile(parsedOrigin.charAt(0)),
                    Rank.findRank(parsedOrigin.charAt(1)));
            Square movedSquare = Square.of(
                    File.findFile(parsedMoved.charAt(0)),
                    Rank.findRank(parsedMoved.charAt(1)));
            chessBoard.cancelMove(originSquare, movedSquare, piece);
            currentCamp = currentCamp.fetchOppositeCamp();
        }
        gameDao.deleteLatestTwoHistory(roomId);
        saveGameInfo(roomId, generateGameInfoDto());
    }

    public void end(long roomId) {
        if (!isOngoing) {
            throw new IllegalStateException("start를 먼저 입력해주세요.");
        }
        isOngoing = false;
        GameInfoDto gameInfoDto = generateGameInfoDto();
        saveGameInfo(roomId, gameInfoDto);
    }

    private void saveGameInfo(long roomId, GameInfoDto gameInfoDto) {
        gameDao.deleteBoardById(roomId);
        gameDao.updateCurrentTurn(roomId, gameInfoDto.getCurrentTurn());
        gameDao.saveBoard(roomId, gameInfoDto.getBoardDtos());
    }

    private GameInfoDto generateGameInfoDto() {
        Map<Square, Piece> board = chessBoard.getBoard();
        List<BoardDto> boardDtos = board.keySet().stream()
                .map(square -> BoardDto.of(square, board.get(square)))
                .collect(Collectors.toUnmodifiableList());
        return new GameInfoDto(currentCamp.name(), boardDtos);
    }

    private Square getCurrentSquare(String currentSquareInput) {
        return Square.of(File.findFile(currentSquareInput.charAt(0)),
                Rank.findRank(currentSquareInput.charAt(1)));
    }

    private Square getTargetSquare(String targetSquareInput) {
        return Square.of(File.findFile(targetSquareInput.charAt(0)),
                Rank.findRank(targetSquareInput.charAt(1)));
    }

    private void validateCurrentCamp(Square currentSquare) {
        if (!chessBoard.isCorrectCamp(currentCamp, currentSquare)) {
            throw new IllegalStateException("같은 진영의 말만 움직일 수 있습니다.");
        }
    }

    public Map<Square, Piece> getChessBoard() {
        return chessBoard.getBoard();
    }

    public boolean isFinished() {
        return !isOngoing;
    }

    public List<ScoreDto> calculateFinalScore() {
        return Camp.PLAYING_CAMPS.stream()
                .map(camp -> ScoreDto.from(camp, chessBoard.calculateFinalScore(camp)))
                .collect(Collectors.toUnmodifiableList());
    }

    public GameInfoDto getGameInfo(long roomId) {
        return new GameInfoDto(gameDao.findCurrentTurnByGameName(roomId), gameDao.findBoardByRoomId(roomId));
    }
}
