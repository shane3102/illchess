package pl.illchess.adapter.board.command.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.model.PieceBehaviour;
import pl.illchess.domain.piece.model.info.CurrentPlayerColor;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardMapper {

    public static BoardEntity toEntity(Board board) {
        if (board == null) {
            return null;
        } else {
            return new BoardEntity(
                    board.boardId().uuid(),
                    toPiecesLocationsEntity(board.piecesLocations()),
                    board.currentPlayerColor().color().toString(),
                    toMoveHistoryEntity(board.moveHistory())
            );
        }
    }

    public static Board toDomain(BoardEntity entity) {

        if (entity == null) {
            return null;
        } else {
            return new Board(
                    new BoardId(entity.boardId()),
                    toPiecesLocations(entity.piecesLocations()),
                    new CurrentPlayerColor(PieceColor.valueOf(entity.currentPlayerColor())),
                    toMoveHistory(entity.moveStackData())
            );
        }
    }

    private static PiecesLocations toPiecesLocations(Map<String, BoardEntity.PieceEntity> piecesLocationsInEntity) {
        return new PiecesLocations(
                piecesLocationsInEntity.entrySet()
                        .stream()
                        .map(
                                entry -> PieceBehaviour.getPieceByPieceType(
                                        new PieceType(entry.getValue().pieceType()),
                                        PieceColor.valueOf(entry.getValue().pieceColor()),
                                        Square.valueOf(entry.getKey())
                                )
                        )
                        .collect(Collectors.toSet())
        );
    }

    private static MoveHistory toMoveHistory(List<BoardEntity.MoveEntity> moveHistoryList) {
        Stack<Move> moveStack = new Stack<>();
        Stream.iterate(moveHistoryList.size() - 1, i -> i - 1)
                .limit(moveHistoryList.size())
                .forEach(i -> {
                    BoardEntity.MoveEntity moveEntity = moveHistoryList.get(i);

                    Move move = new Move(
                            Square.valueOf(moveEntity.startSquare()),
                            Square.valueOf(moveEntity.targetSquare()),
                            PieceBehaviour.getPieceByPieceType(
                                    new PieceType(moveEntity.movedPiece().pieceType()),
                                    PieceColor.valueOf(moveEntity.movedPiece().pieceColor()),
                                    Square.valueOf(moveEntity.targetSquare())
                            ),
                            moveEntity.capturedPiece() == null
                                    ?
                                    null
                                    :
                                    PieceBehaviour.getPieceByPieceType(
                                            new PieceType(moveEntity.capturedPiece().pieceType()),
                                            PieceColor.valueOf(moveEntity.capturedPiece().pieceColor()),
                                            Square.valueOf(moveEntity.targetSquare())
                                    )
                    );

                    moveStack.push(move);
                });

        return new MoveHistory(moveStack);
    }

    private static Map<String, BoardEntity.PieceEntity> toPiecesLocationsEntity(PiecesLocations piecesLocations) {
        return piecesLocations.locations()
                .stream()
                .map(
                        entry -> Map.entry(
                                entry.square().toString(),
                                new BoardEntity.PieceEntity(
                                        entry.color().toString(),
                                        entry.typeName().text()
                                )
                        )
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<BoardEntity.MoveEntity> toMoveHistoryEntity(MoveHistory moveHistory) {
        return moveHistory.moveStack()
                .stream()
                .map(
                        move -> new BoardEntity.MoveEntity(
                                move.startSquare().toString(),
                                move.targetSquare().toString(),
                                new BoardEntity.PieceEntity(
                                        move.movedPiece().color().toString(),
                                        move.movedPiece().typeName().text()
                                ),
                                move.capturedPiece() == null ?
                                        null :
                                        new BoardEntity.PieceEntity(
                                                move.capturedPiece().color().toString(),
                                                move.capturedPiece().typeName().text()
                                        )
                        )
                ).toList();
    }
}
