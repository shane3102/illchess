package pl.illchess.adapter.board.query.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.application.board.query.out.model.MoveView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardViewMapper {

    public static BoardView toView(BoardEntity entity) {
        if (entity == null) {
            return null;
        } else {
            return new BoardView(
                entity.boardId(),
                toPiecesLocations(entity.piecesLocations()),
                entity.boardState().currentPlayerColor(),
                entity.boardState().player1().username(),
                entity.boardState().player2() == null ? null : entity.boardState().player2().username(),
                entity.boardState().gameState(),
                entity.boardState().victoriousPlayerColor(),
                toLastPerformedMove(entity)
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

    private static Map<String, BoardView.PieceView> toPiecesLocations(
        List<BoardEntity.PieceEntity> piecesLocationsInEntity
    ) {
        return piecesLocationsInEntity.stream()
            .map(
                pieceWithLocation -> Map.entry(
                    pieceWithLocation.square(),
                    new BoardView.PieceView(
                        pieceWithLocation.pieceColor(),
                        pieceWithLocation.pieceType()
                    )
                )
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
