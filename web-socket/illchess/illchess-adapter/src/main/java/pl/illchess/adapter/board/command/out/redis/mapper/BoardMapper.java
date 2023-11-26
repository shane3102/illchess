package pl.illchess.adapter.board.command.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.piece.Piece;
import pl.illchess.domain.piece.info.PieceColor;
import pl.illchess.domain.piece.info.PieceType;

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
                    board.currentPlayerColor().toString(),
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
                    PieceColor.valueOf(entity.currentPlayerColor()),
                    toMoveHistory(entity.moveStackData())
            );
        }
    }

    private static PiecesLocations toPiecesLocations(Map<String, BoardEntity.PieceEntity> piecesLocationsInEntity) {
        return new PiecesLocations(
                piecesLocationsInEntity.entrySet()
                        .stream()
                        .map(
                                entry -> Map.entry(
                                        Square.valueOf(entry.getKey()),
                                        new Piece(PieceColor.valueOf(entry.getValue().pieceColor()), PieceType.valueOf(entry.getValue().pieceType()))
                                )
                        )
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

    private static MoveHistory toMoveHistory(List<BoardEntity.MoveEntity> moveHistoryList) {
        Stack<Move> moveStack = new Stack<>();
        Stream.iterate(moveHistoryList.size() - 1, i -> i + 1)
                .limit(moveHistoryList.size())
                .forEach(i -> {
                    BoardEntity.MoveEntity moveEntity = moveHistoryList.get(i);

                    Move move = new Move(
                            Square.valueOf(moveEntity.startSquare()),
                            Square.valueOf(moveEntity.targetSquare()),
                            new Piece(PieceColor.valueOf(moveEntity.movedPiece().pieceColor()), PieceType.valueOf(moveEntity.movedPiece().pieceType())),
                            new Piece(PieceColor.valueOf(moveEntity.capturedPiece().pieceColor()), PieceType.valueOf(moveEntity.capturedPiece().pieceType()))
                    );

                    moveStack.push(move);
                });

        return new MoveHistory(moveStack);
    }

    private static Map<String, BoardEntity.PieceEntity> toPiecesLocationsEntity(PiecesLocations piecesLocations) {
        return piecesLocations.locations()
                .entrySet()
                .stream()
                .map(
                        entry -> Map.entry(
                                entry.getKey().toString(),
                                new BoardEntity.PieceEntity(
                                        entry.getValue().color().toString(),
                                        entry.getValue().type().toString()
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
                                        move.movedPiece().type().toString()
                                ),
                                new BoardEntity.PieceEntity(
                                        move.capturedPiece().color().toString(),
                                        move.capturedPiece().type().toString()
                                )
                        )
                ).toList();
    }
}
