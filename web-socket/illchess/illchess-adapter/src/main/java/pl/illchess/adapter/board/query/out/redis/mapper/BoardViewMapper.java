package pl.illchess.adapter.board.query.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.application.board.query.out.model.BoardView;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

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
                    PieceColor.valueOf(entity.currentPlayerColor())
            );
        }
    }

    private static Map<Square, BoardView.PieceView> toPiecesLocations(
            Map<String, BoardEntity.PieceEntity> piecesLocationsInEntity
    ) {
        return piecesLocationsInEntity.entrySet()
                .stream()
                .map(
                        entry -> Map.entry(
                                Square.valueOf(entry.getKey()),
                                new BoardView.PieceView(
                                        PieceColor.valueOf(entry.getValue().pieceColor()),
                                        entry.getValue().pieceType()
                                )
                        )
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
