package pl.illchess.game.adapter.board.query.out.redis.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.illchess.game.adapter.board.command.out.redis.model.GameEntity;
import pl.illchess.game.application.game.query.out.model.GameAdditionalInfoView;
import pl.illchess.game.application.game.query.out.model.GameFinishedView;
import pl.illchess.game.application.game.query.out.model.GameFinishedView.PerformedMovesGameFinishedView;
import pl.illchess.game.application.game.query.out.model.GameView;
import pl.illchess.game.application.game.query.out.model.GameWithPreMovesView;
import pl.illchess.game.application.game.query.out.model.MoveView;
import pl.illchess.game.application.game.query.out.model.PieceView;
import pl.illchess.game.application.game.query.out.model.PlayerView;
import pl.illchess.game.domain.piece.model.info.PieceType;

public class BoardViewMapper {

    public static GameWithPreMovesView toViewAsPreMove(GameEntity entity, List<GameEntity.PreMoveEntity> userPreMoves, List<GameEntity.PieceEntity> pieces) {
        if (entity == null) {
            return null;
        } else {
            return new GameWithPreMovesView(
                entity.gameId(),
                toPiecesLocations(pieces),
                toLastPerformedMove(entity),
                userPreMoves.stream().map(preMoveEntity -> new MoveView(preMoveEntity.startSquare(), preMoveEntity.targetSquare())).toList(),
                entity.boardState().whitePlayer().username(),
                entity.boardState().blackPlayer() == null ? null : entity.boardState().blackPlayer().username()
            );
        }
    }

    public static GameView toView(GameEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new GameView(
                entity.gameId(),
                toPiecesLocations(entity.piecesLocations()),
                toLastPerformedMove(entity),
                entity.boardState().whitePlayer().username(),
                entity.boardState().blackPlayer() == null ? null : entity.boardState().blackPlayer().username()
            );
        }
    }

    public static GameAdditionalInfoView toAdditionalInfoView(GameEntity entity) {
        if (entity == null) {
            return null;
        } else {

            Supplier<Stream<GameEntity.MoveEntity>> capturedPiecesStreamSupplier = () -> entity.moveStackData().stream().filter(it -> it.capturedPiece() != null);

            return new GameAdditionalInfoView(
                entity.gameId(),
                entity.boardState().currentPlayerColor(),
                new PlayerView(
                    entity.boardState().whitePlayer().username(),
                    entity.boardState().whitePlayer().isProposingDraw(),
                    entity.boardState().whitePlayer().isProposingTakingBackMove()
                ),
                entity.boardState().blackPlayer() == null
                    ? null
                    : new PlayerView(
                    entity.boardState().blackPlayer().username(),
                    entity.boardState().blackPlayer().isProposingDraw(),
                    entity.boardState().blackPlayer().isProposingTakingBackMove()
                ),
                entity.boardState().gameState(),
                entity.boardState().gameResultCause(),
                capturedPiecesStreamSupplier.get()
                    .filter(move -> "WHITE".equals(move.capturedPiece().pieceColor()))
                    .map(move -> move.capturedPiece().pieceType())
                    .sorted(PieceType::pieceTypeComparator)
                    .toList(),
                capturedPiecesStreamSupplier.get()
                    .filter(move -> "BLACK".equals(move.capturedPiece().pieceColor()))
                    .map(move -> move.capturedPiece().pieceType())
                    .sorted(PieceType::pieceTypeComparator)
                    .toList(),
                toPerformedMoves(entity.moveStackData())
            );
        }
    }

    public static GameFinishedView toBoardWithFinishedGameView(GameEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new GameFinishedView(
                entity.gameId(),
                entity.boardState().whitePlayer().username(),
                entity.boardState().blackPlayer().username(),
                entity.boardState().gameState(),
                entity.boardState().gameResultCause(),
                LocalDateTime.now(),
                entity.moveStackData().stream().map(
                    it -> new PerformedMovesGameFinishedView(
                        it.startSquare(),
                        it.targetSquare(),
                        toPerformedMove(it),
                        it.movedPiece().pieceColor()
                    )
                ).toList()
            );
        }
    }

    private static MoveView toLastPerformedMove(GameEntity entity) {
        return entity.moveStackData() == null || entity.moveStackData().isEmpty()
            ? null
            : new MoveView(
            entity.moveStackData().get(entity.moveStackData().size() - 1).startSquare(),
            entity.moveStackData().get(entity.moveStackData().size() - 1).targetSquare()
        );
    }

    private static Map<String, PieceView> toPiecesLocations(
        List<GameEntity.PieceEntity> piecesLocationsInEntity
    ) {
        return piecesLocationsInEntity.stream()
            .map(
                pieceWithLocation -> Map.entry(
                    pieceWithLocation.square(),
                    new PieceView(
                        pieceWithLocation.pieceColor(),
                        pieceWithLocation.pieceType()
                    )
                )
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<String> toPerformedMoves(List<GameEntity.MoveEntity> moveEntities) {
        return moveEntities.stream().map(BoardViewMapper::toPerformedMove).toList();
    }

    private static String toPerformedMove(GameEntity.MoveEntity moveEntity) {

        String startSquareLower = moveEntity.startSquare().toLowerCase();
        String targetSquareLower = moveEntity.targetSquare().toLowerCase();

        return switch (moveEntity.movedPiece().pieceType()) {
            case "PAWN" -> {
                if (moveEntity.capturedPiece() != null) {
                    yield startSquareLower.charAt(0) + "x" + targetSquareLower;
                }
                yield targetSquareLower;
            }
            case "BISHOP", "KNIGHT", "ROOK", "QUEEN", "KING" -> {
                String result = getPieceLetter(moveEntity.movedPiece().pieceType()) + startSquareLower;
                if (moveEntity.capturedPiece() != null) {
                    yield result + 'x' + targetSquareLower;
                }
                yield result + targetSquareLower;
            }
            default -> "";
        };
    }

    private static String getPieceLetter(String pieceType) {
        return switch (pieceType) {
            case "BISHOP" -> "B";
            case "KNIGHT" -> "N";
            case "ROOK" -> "R";
            case "QUEEN" -> "Q";
            case "KING" -> "K";
            default -> "";
        };
    }
}
