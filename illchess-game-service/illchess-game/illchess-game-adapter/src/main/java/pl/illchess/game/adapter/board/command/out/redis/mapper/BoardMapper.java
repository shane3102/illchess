package pl.illchess.game.adapter.board.command.out.redis.mapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity.PreMoveEntity;
import pl.illchess.game.domain.game.model.Game;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.game.model.FenBoardString;
import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.MoveHistory;
import pl.illchess.game.domain.game.model.square.Board;
import pl.illchess.game.domain.game.model.square.Square;
import pl.illchess.game.domain.game.model.state.GameInfo;
import pl.illchess.game.domain.game.model.state.GameState;
import pl.illchess.game.domain.game.model.state.GameResultCause;
import pl.illchess.game.domain.game.model.state.GameStartTime;
import pl.illchess.game.domain.game.model.state.player.IsProposingDraw;
import pl.illchess.game.domain.game.model.state.player.IsProposingTakingBackMove;
import pl.illchess.game.domain.game.model.state.player.Player;
import pl.illchess.game.domain.game.model.state.player.PreMove;
import pl.illchess.game.domain.game.model.state.player.Username;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.info.PieceType;

public class BoardMapper {

    public static GameEntity toEntity(Game game) {
        if (game == null) {
            return null;
        } else {
            return new GameEntity(
                game.gameId() == null ? UUID.randomUUID() : game.gameId().uuid(),
                toPiecesLocationsEntity(game.board()),
                toMoveHistoryEntity(game.moveHistory()),
                toBoardStateEntity(game.gameInfo())
            );
        }
    }

    private static GameEntity.GameInfoEntity toBoardStateEntity(GameInfo gameInfo) {
        return new GameEntity.GameInfoEntity(
            gameInfo.currentPlayerColor().color().toString(),
            mapToPlayer(gameInfo.whitePlayer()),
            mapToPlayer(gameInfo.blackPlayer()),
            gameInfo.gameResult().toString(),
            gameInfo.gameResultCause() == null ? null : gameInfo.gameResultCause().toString(),
            gameInfo.gameStartTime().value()
        );
    }

    private static GameEntity.PlayerEntity mapToPlayer(Player player) {
        return player == null
            ? null
            : new GameEntity.PlayerEntity(
                player.username().text(),
                player.isProposingDraw().value(),
                player.isProposingTakingBackMove().value(),
                player.preMoves().stream().map(
                    it -> new PreMoveEntity(
                        it.startSquare().name(),
                        it.targetSquare().name(),
                        it.pawnPromotedToPieceType() == null ? null : it.pawnPromotedToPieceType().text(),
                        toPiecesLocationsEntity(it.boardAfterPreMove())
                    )
                )
                .toList()
        );
    }

    public static Game toDomain(GameEntity entity) {

        if (entity == null) {
            return null;
        } else {
            return new Game(
                new GameId(entity.gameId()),
                toPiecesLocations(entity.piecesLocations()),
                toMoveHistory(entity.moveStackData()),
                toBoardState(entity.boardState())
            );
        }
    }

    private static GameInfo toBoardState(GameEntity.GameInfoEntity boardState) {
        return GameInfo.of(
            PieceColor.valueOf(boardState.currentPlayerColor()),
            GameState.valueOf(boardState.gameState()),
            boardState.gameResultCause() == null ? null : GameResultCause.valueOf(boardState.gameResultCause()),
            mapToPlayer(boardState.whitePlayer()),
            mapToPlayer(boardState.blackPlayer()),
            new GameStartTime(boardState.startTime())
        );
    }

    private static Player mapToPlayer(GameEntity.PlayerEntity player) {
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

    private static Board toPiecesLocations(List<GameEntity.PieceEntity> piecesLocationsInEntity) {
        return new Board(
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

    private static MoveHistory toMoveHistory(List<GameEntity.MoveEntity> moveHistoryList) {
        Stack<Move> moveStack = new Stack<>();
        Stream.iterate(0, i -> i + 1)
            .limit(moveHistoryList.size())
            .forEach(i -> {
                GameEntity.MoveEntity moveEntity = moveHistoryList.get(i);

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

    private static List<GameEntity.PieceEntity> toPiecesLocationsEntity(Board board) {
        return board.piecesLocations()
            .stream()
            .map(
                piece -> new GameEntity.PieceEntity(
                    piece.square().toString(),
                    piece.color().toString(),
                    piece.typeName().text(),
                    piece.cachedReachableSquares() == null ? Set.of() : piece.cachedReachableSquares().stream().map(Enum::name).collect(Collectors.toSet())
                )
            )
            .toList();
    }

    private static List<GameEntity.MoveEntity> toMoveHistoryEntity(MoveHistory moveHistory) {
        return moveHistory.moveStack()
            .stream()
            .map(
                move -> new GameEntity.MoveEntity(
                    move.startSquare().toString(),
                    move.targetSquare().toString(),
                    new GameEntity.PieceEntity(
                        move.movedPiece().square().toString(),
                        move.movedPiece().color().toString(),
                        move.movedPiece().typeName().text(),
                        Set.of()
                    ),
                    move.capturedPiece() == null ?
                        null :
                        new GameEntity.PieceEntity(
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
