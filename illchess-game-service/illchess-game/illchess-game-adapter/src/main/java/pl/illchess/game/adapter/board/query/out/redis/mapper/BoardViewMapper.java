package pl.illchess.game.adapter.board.query.out.redis.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import pl.illchess.game.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.game.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.game.application.board.query.out.model.BoardGameFinishedView;
import pl.illchess.game.application.board.query.out.model.BoardGameFinishedView.PerformedMovesGameFinishedView;
import pl.illchess.game.application.board.query.out.model.BoardView;
import pl.illchess.game.application.board.query.out.model.BoardWithPreMovesView;
import pl.illchess.game.application.board.query.out.model.MoveView;
import pl.illchess.game.application.board.query.out.model.PieceView;
import pl.illchess.game.application.board.query.out.model.PlayerView;
import pl.illchess.game.domain.piece.model.info.PieceType;

public class BoardViewMapper {

    public static BoardWithPreMovesView toViewAsPreMove(BoardEntity entity, List<BoardEntity.PreMoveEntity> userPreMoves, List<BoardEntity.PieceEntity> pieces) {
        if (entity == null) {
            return null;
        } else {
            return new BoardWithPreMovesView(
                entity.boardId(),
                toPiecesLocations(pieces),
                toLastPerformedMove(entity),
                userPreMoves.stream().map(preMoveEntity -> new MoveView(preMoveEntity.startSquare(), preMoveEntity.targetSquare())).toList(),
                entity.boardState().whitePlayer().username(),
                entity.boardState().blackPlayer() == null ? null : entity.boardState().blackPlayer().username()
            );
        }
    }

    public static BoardView toView(BoardEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new BoardView(
                entity.boardId(),
                toPiecesLocations(entity.piecesLocations()),
                toLastPerformedMove(entity),
                entity.boardState().whitePlayer().username(),
                entity.boardState().blackPlayer() == null ? null : entity.boardState().blackPlayer().username()
            );
        }
    }

    public static BoardAdditionalInfoView toAdditionalInfoView(BoardEntity entity) {
        if (entity == null) {
            return null;
        } else {

            Supplier<Stream<BoardEntity.MoveEntity>> capturedPiecesStreamSupplier = () -> entity.moveStackData().stream().filter(it -> it.capturedPiece() != null);

            return new BoardAdditionalInfoView(
                entity.boardId(),
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
                entity.boardState().victoriousPlayerColor(),
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

    public static BoardGameFinishedView toBoardWithFinishedGameView(BoardEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new BoardGameFinishedView(
                entity.boardId(),
                entity.boardState().whitePlayer().username(),
                entity.boardState().blackPlayer().username(),
                toGameResult(entity.boardState().victoriousPlayerColor()),
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

    private static String toGameResult(String victoriousColor) {
        return victoriousColor == null ? "DRAW" : switch (victoriousColor) {
            case "WHITE" -> "WHITE_WON";
            case "BLACK" -> "BLACK_WON";
            default -> "DRAW";
        };
    }

    private static MoveView toLastPerformedMove(BoardEntity entity) {
        return entity.moveStackData() == null || entity.moveStackData().isEmpty()
            ? null
            : new MoveView(
            entity.moveStackData().get(entity.moveStackData().size() - 1).startSquare(),
            entity.moveStackData().get(entity.moveStackData().size() - 1).targetSquare()
        );
    }

    private static Map<String, PieceView> toPiecesLocations(
        List<BoardEntity.PieceEntity> piecesLocationsInEntity
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

    private static List<String> toPerformedMoves(List<BoardEntity.MoveEntity> moveEntities) {
        return moveEntities.stream().map(BoardViewMapper::toPerformedMove).toList();
    }

    private static String toPerformedMove(BoardEntity.MoveEntity moveEntity) {

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
