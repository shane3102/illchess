package pl.illchess.adapter.board.query.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.application.board.query.out.model.BoardView;

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
                    entity.currentPlayerColor()
            );
        }
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
