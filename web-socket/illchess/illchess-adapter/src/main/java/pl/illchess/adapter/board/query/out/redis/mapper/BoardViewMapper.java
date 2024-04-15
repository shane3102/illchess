package pl.illchess.adapter.board.query.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.application.board.query.out.model.BoardAdditionalInfoView;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.application.board.query.out.model.MoveView;
import pl.illchess.application.board.query.out.model.PieceView;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardViewMapper {

    public static BoardView toView(BoardEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new BoardView(
                entity.boardId(),
                toPiecesLocations(entity.piecesLocations()),
                toLastPerformedMove(entity)
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
                entity.boardState().player1().username(),
                entity.boardState().player2() == null ? null : entity.boardState().player2().username(),
                entity.boardState().gameState(),
                entity.boardState().victoriousPlayerColor(),
                capturedPiecesStreamSupplier.get()
                        .filter(move-> "WHITE".equals(move.capturedPiece().pieceColor()))
                        .map(move -> move.capturedPiece().pieceType())
                        .sorted(PieceType::pieceTypeComparator)
                        .toList(),
                capturedPiecesStreamSupplier.get()
                        .filter(move-> "BLACK".equals(move.capturedPiece().pieceColor()))
                        .map(move -> move.capturedPiece().pieceType())
                        .sorted(PieceType::pieceTypeComparator)
                        .toList(),
                toPerformedMoves(entity.moveStackData())
            );
        }
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
