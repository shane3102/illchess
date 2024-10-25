package pl.illchess.game.adapter.board.command.out.redis.mapper;

import pl.illchess.game.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.game.adapter.board.command.out.redis.model.BoardEntity.PreMoveEntity;
import pl.illchess.game.domain.board.model.Board;
import pl.illchess.game.domain.board.model.BoardId;
import pl.illchess.game.domain.board.model.FenBoardString;
import pl.illchess.game.domain.board.model.history.Move;
import pl.illchess.game.domain.board.model.history.MoveHistory;
import pl.illchess.game.domain.board.model.square.PiecesLocations;
import pl.illchess.game.domain.board.model.square.Square;
import pl.illchess.game.domain.board.model.state.BoardState;
import pl.illchess.game.domain.board.model.state.GameState;
import pl.illchess.game.domain.board.model.state.player.IsProposingDraw;
import pl.illchess.game.domain.board.model.state.player.IsProposingTakingBackMove;
import pl.illchess.game.domain.board.model.state.player.Player;
import pl.illchess.game.domain.board.model.state.player.PreMove;
import pl.illchess.game.domain.board.model.state.player.Username;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardMapper {

    public static BoardEntity toEntity(Board board) {
        if (board == null) {
            return null;
        } else {
            return new BoardEntity(
                board.boardId() == null ? UUID.randomUUID() : board.boardId().uuid(),
                toPiecesLocationsEntity(board.piecesLocations()),
                toMoveHistoryEntity(board.moveHistory()),
                toBoardStateEntity(board.boardState())
            );
        }
    }

    private static BoardEntity.BoardStateEntity toBoardStateEntity(BoardState boardState) {
        return new BoardEntity.BoardStateEntity(
            boardState.currentPlayerColor().color().toString(),
            mapToPlayer(boardState.whitePlayer()),
            mapToPlayer(boardState.blackPlayer()),
            boardState.gameState().toString(),
            boardState.victoriousPlayerColor() == null ? null : boardState.victoriousPlayerColor().toString()
        );
    }

    private static BoardEntity.PlayerEntity mapToPlayer(Player player) {
        return player == null
            ? null
            : new BoardEntity.PlayerEntity(
                player.username().text(),
                player.isProposingDraw().value(),
                player.isProposingTakingBackMove().value(),
                player.preMoves().stream().map(
                    it -> new PreMoveEntity(
                        it.startSquare().name(),
                        it.targetSquare().name(),
                        it.pawnPromotedToPieceType() == null ? null : it.pawnPromotedToPieceType().text(),
                        toPiecesLocationsEntity(it.piecesLocationsAfterPreMove())
                    )
                )
                .toList()
        );
    }

    public static Board toDomain(BoardEntity entity) {

        if (entity == null) {
            return null;
        } else {
            return new Board(
                new BoardId(entity.boardId()),
                toPiecesLocations(entity.piecesLocations()),
                toMoveHistory(entity.moveStackData()),
                toBoardState(entity.boardState())
            );
        }
    }

    private static BoardState toBoardState(BoardEntity.BoardStateEntity boardState) {
        return BoardState.of(
            PieceColor.valueOf(boardState.currentPlayerColor()),
            GameState.valueOf(boardState.gameState()),
            mapToPlayer(boardState.whitePlayer()),
            mapToPlayer(boardState.blackPlayer()),
            boardState.victoriousPlayerColor() == null ? null : PieceColor.valueOf(boardState.victoriousPlayerColor())
        );
    }

    private static Player mapToPlayer(BoardEntity.PlayerEntity player) {
        return player == null
            ? null
            :
            new Player(
                new Username(player.username()),
                new IsProposingDraw(player.isProposingDraw()),
                new IsProposingTakingBackMove(player.isProposingTakingBackMove()),
                new LinkedList<>(
                    player.preMoves() == null ? new ArrayList<>() : player.preMoves()
                        .stream()
                        .map(
                            it -> new PreMove(
                                Square.valueOf(it.startSquare()),
                                Square.valueOf(it.targetSquare()),
                                new PieceType(it.pawnPromotedToPieceType()),
                                toPiecesLocations(it.piecesLocationsAfterPreMove())
                            )
                        ).toList()
                )
            );
    }

    private static PiecesLocations toPiecesLocations(List<BoardEntity.PieceEntity> piecesLocationsInEntity) {
        return new PiecesLocations(
            piecesLocationsInEntity.stream()
                .map(
                    piece -> PieceType.getPieceByPieceType(
                        new PieceType(piece.pieceType()),
                        PieceColor.valueOf(piece.pieceColor()),
                        Square.valueOf(piece.square()),
                        piece.cachedReachableSquares() == null ? Set.of() : piece.cachedReachableSquares().stream().map(Square::valueOf).collect(Collectors.toSet())
                    )
                )
                .collect(Collectors.toSet())
        );
    }

    private static MoveHistory toMoveHistory(List<BoardEntity.MoveEntity> moveHistoryList) {
        Stack<Move> moveStack = new Stack<>();
        Stream.iterate(0, i -> i + 1)
            .limit(moveHistoryList.size())
            .forEach(i -> {
                BoardEntity.MoveEntity moveEntity = moveHistoryList.get(i);

                Move move = new Move(
                    Square.valueOf(moveEntity.startSquare()),
                    Square.valueOf(moveEntity.targetSquare()),
                    PieceType.getPieceByPieceType(
                        new PieceType(moveEntity.movedPiece().pieceType()),
                        PieceColor.valueOf(moveEntity.movedPiece().pieceColor()),
                        Square.valueOf(moveEntity.targetSquare()),
                        Set.of()
                    ),
                    moveEntity.capturedPiece() == null
                        ?
                        null
                        :
                        PieceType.getPieceByPieceType(
                            new PieceType(moveEntity.capturedPiece().pieceType()),
                            PieceColor.valueOf(moveEntity.capturedPiece().pieceColor()),
                            Square.valueOf(moveEntity.targetSquare()),
                            Set.of()
                        ),
                    FenBoardString.fromString(moveEntity.fenString())
                );

                moveStack.push(move);
            });

        return new MoveHistory(moveStack);
    }

    private static List<BoardEntity.PieceEntity> toPiecesLocationsEntity(PiecesLocations piecesLocations) {
        return piecesLocations.locations()
            .stream()
            .map(
                piece -> new BoardEntity.PieceEntity(
                    piece.square().toString(),
                    piece.color().toString(),
                    piece.typeName().text(),
                    piece.cachedReachableSquares() == null ? Set.of() : piece.cachedReachableSquares().stream().map(Enum::name).collect(Collectors.toSet())
                )
            )
            .toList();
    }

    private static List<BoardEntity.MoveEntity> toMoveHistoryEntity(MoveHistory moveHistory) {
        return moveHistory.moveStack()
            .stream()
            .map(
                move -> new BoardEntity.MoveEntity(
                    move.startSquare().toString(),
                    move.targetSquare().toString(),
                    new BoardEntity.PieceEntity(
                        move.movedPiece().square().toString(),
                        move.movedPiece().color().toString(),
                        move.movedPiece().typeName().text(),
                        Set.of()
                    ),
                    move.capturedPiece() == null ?
                        null :
                        new BoardEntity.PieceEntity(
                            move.capturedPiece().square().toString(),
                            move.capturedPiece().color().toString(),
                            move.capturedPiece().typeName().text(),
                            Set.of()
                        ),
                    move.fenBoardStringBeforeMove().fullString()
                )
            )
            .toList();
    }
}
