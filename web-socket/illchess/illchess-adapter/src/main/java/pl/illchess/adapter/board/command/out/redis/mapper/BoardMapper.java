package pl.illchess.adapter.board.command.out.redis.mapper;

import pl.illchess.adapter.board.command.out.redis.model.BoardEntity;
import pl.illchess.domain.board.model.Board;
import pl.illchess.domain.board.model.BoardId;
import pl.illchess.domain.board.model.history.IsCastling;
import pl.illchess.domain.board.model.history.IsEnPassant;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.history.MoveHistory;
import pl.illchess.domain.board.model.square.PiecesLocations;
import pl.illchess.domain.board.model.square.Square;
import pl.illchess.domain.board.model.state.BoardState;
import pl.illchess.domain.board.model.state.GameState;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.info.PieceType;

import java.util.List;
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
                toMoveHistoryEntity(board.moveHistory()),
                toBoardStateEntity(board.boardState())
            );
        }
    }

    private static BoardEntity.BoardStateEntity toBoardStateEntity(BoardState boardState) {
        return new BoardEntity.BoardStateEntity(
            boardState.currentPlayerColor().color().toString(),
            boardState.gameState().toString(),
            boardState.victoriousPlayerColor() == null ? null : boardState.victoriousPlayerColor().toString()
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
            boardState.victoriousPlayerColor() == null ? null : PieceColor.valueOf(boardState.victoriousPlayerColor())
        );
    }

    private static PiecesLocations toPiecesLocations(List<BoardEntity.PieceEntity> piecesLocationsInEntity) {
        return new PiecesLocations(
            piecesLocationsInEntity.stream()
                .map(
                    piece -> PieceType.getPieceByPieceType(
                        new PieceType(piece.pieceType()),
                        PieceColor.valueOf(piece.pieceColor()),
                        Square.valueOf(piece.square())
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
                        Square.valueOf(moveEntity.targetSquare())
                    ),
                    moveEntity.capturedPiece() == null
                        ?
                        null
                        :
                        PieceType.getPieceByPieceType(
                            new PieceType(moveEntity.capturedPiece().pieceType()),
                            PieceColor.valueOf(moveEntity.capturedPiece().pieceColor()),
                            Square.valueOf(moveEntity.targetSquare())
                        ),
                    new IsEnPassant(moveEntity.isEnPassant()),
                    new IsCastling(moveEntity.isCastling())
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
                    piece.typeName().text()
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
                        move.movedPiece().typeName().text()
                    ),
                    move.capturedPiece() == null ?
                        null :
                        new BoardEntity.PieceEntity(
                            move.capturedPiece().square().toString(),
                            move.capturedPiece().color().toString(),
                            move.capturedPiece().typeName().text()
                        ),
                    move.isEnPassant().value(),
                    move.isCastling().value()
                )
            )
            .toList();
    }
}
