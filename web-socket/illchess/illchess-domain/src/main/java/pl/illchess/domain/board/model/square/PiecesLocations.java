package pl.illchess.domain.board.model.square;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.exception.TargetSquareOccupiedBySameColorPieceException;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.piece.model.Piece;
import pl.illchess.domain.piece.model.PieceCapableOfPinning;
import pl.illchess.domain.piece.model.info.PieceColor;
import pl.illchess.domain.piece.model.type.Bishop;
import pl.illchess.domain.piece.model.type.King;
import pl.illchess.domain.piece.model.type.Knight;
import pl.illchess.domain.piece.model.type.Pawn;
import pl.illchess.domain.piece.model.type.Queen;
import pl.illchess.domain.piece.model.type.Rook;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.illchess.domain.piece.model.info.PieceColor.BLACK;
import static pl.illchess.domain.piece.model.info.PieceColor.WHITE;

public record PiecesLocations(
        Set<Piece> locations
) {

    public Move movePiece(MovePiece command, Move lastPerformedMove) {
        Square movedPieceStartSquare = command.movedPiece().square();

        if (!isPieceOnLocation(command)) {
            throw new PieceNotPresentOnGivenSquare(
                    command.boardId(),
                    command.movedPiece(),
                    command.movedPiece().square()
            );
        }

        if (isSquareOccupiedBySameColorPiece(command)) {
            throw new TargetSquareOccupiedBySameColorPieceException(
                    command.boardId(),
                    command.movedPiece().square(),
                    command.targetSquare()
            );
        }

        Piece capturedPiece = getPieceOnSquare(command.targetSquare()).orElse(null);

        removePieceIfEnPassantMove(command, lastPerformedMove);
        movePieceMechanic(command);

        return new Move(
                movedPieceStartSquare,
                command.targetSquare(),
                command.movedPiece(),
                capturedPiece
        );
    }

    public void takeBackMove(Move moveTakenBack) {

        locations.removeIf(
                piece -> Objects.equals(piece.square(), moveTakenBack.startSquare()) ||
                        Objects.equals(piece.square(), moveTakenBack.targetSquare())
        );

        moveTakenBack.movedPiece().setSquare(moveTakenBack.startSquare());
        locations.add(moveTakenBack.movedPiece());

        if (moveTakenBack.capturedPiece() != null) {
            moveTakenBack.capturedPiece().setSquare(moveTakenBack.targetSquare());
            locations.add(moveTakenBack.capturedPiece());
        }
    }

    public Optional<Piece> getPieceByTypeAndColor(Class<? extends Piece> pieceType, PieceColor pieceColor) {
        return locations.stream()
                .filter(piece ->
                        Objects.equals(piece.getClass(), pieceType)
                                && Objects.equals(piece.color(), pieceColor)
                )
                .findFirst();
    }

    public Optional<Piece> getPieceOnSquare(Square square) {
        return locations.stream()
                .filter(piece -> Objects.equals(piece.square(), square))
                .findFirst();
    }

    public Set<Piece> getEnemyPieces(PieceColor color) {
        return locations.stream()
                .filter(piece -> !Objects.equals(piece.color(), color))
                .collect(Collectors.toSet());
    }

    public Set<PieceCapableOfPinning> getEnemyPiecesCapableOfPinning(PieceColor color) {
        return locations.stream()
                .filter(piece -> !Objects.equals(piece.color(), color))
                .filter(piece -> piece instanceof PieceCapableOfPinning)
                .map(piece -> (PieceCapableOfPinning) piece)
                .collect(Collectors.toSet());
    }

    private boolean isPieceOnLocation(MovePiece command) {
        return Objects.equals(getPieceOnSquare(command.movedPiece().square()).orElse(null), command.movedPiece());
    }

    private boolean isSquareOccupiedBySameColorPiece(MovePiece command) {
        Optional<Piece> possiblePiece = getPieceOnSquare(command.targetSquare());

        if (possiblePiece.isEmpty()) {
            return false;
        }

        PieceColor movedPieceColor = command.movedPiece().color();

        return Objects.equals(possiblePiece.get().color(), movedPieceColor);
    }

    private void movePieceMechanic(MovePiece command) {
        locations.removeIf(
                piece -> Objects.equals(piece.square(), command.movedPiece().square()) ||
                        Objects.equals(piece.square(), command.targetSquare())
        );

        command.movedPiece().setSquare(command.targetSquare());

        locations.add(command.movedPiece());
    }

    private void removePieceIfEnPassantMove(MovePiece command, Move lastPerformedMove) {
        if (command.movedPiece() instanceof Pawn pawn) {
            Square square = pawn.getSquareOfPieceCapturedEnPassant(command.targetSquare(), lastPerformedMove);
            if (square != null) {
                locations.removeIf(piece -> Objects.equals(piece.square(), square));
            }
        }

    }

    public static PiecesLocations createBasicBoard() {
        return new PiecesLocations(
                new HashSet<>(
                        Set.of(
                                new Rook(WHITE, Square.A1),
                                new Knight(WHITE, Square.B1),
                                new Bishop(WHITE, Square.C1),
                                new Queen(WHITE, Square.D1),
                                new King(WHITE, Square.E1),
                                new Bishop(WHITE, Square.F1),
                                new Knight(WHITE, Square.G1),
                                new Rook(WHITE, Square.H1),

//                                new Pawn(WHITE, Square.A2),
//                                new Pawn(WHITE, Square.B2),
//                                new Pawn(WHITE, Square.C2),
//                                new Pawn(WHITE, Square.D2),
//                                new Pawn(WHITE, Square.E2),
//                                new Pawn(WHITE, Square.F2),
//                                new Pawn(WHITE, Square.G2),
//                                new Pawn(WHITE, Square.H2),
//
//                                new Pawn(BLACK, Square.A7),
//                                new Pawn(BLACK, Square.B7),
//                                new Pawn(BLACK, Square.C7),
//                                new Pawn(BLACK, Square.D7),
//                                new Pawn(BLACK, Square.E7),
//                                new Pawn(BLACK, Square.F7),
//                                new Pawn(BLACK, Square.G7),
//                                new Pawn(BLACK, Square.H7),

                                new Rook(BLACK, Square.A8),
                                new Knight(BLACK, Square.B8),
                                new Bishop(BLACK, Square.C8),
                                new Queen(BLACK, Square.D8),
                                new King(BLACK, Square.E8),
                                new Bishop(BLACK, Square.F8),
                                new Knight(BLACK, Square.G8),
                                new Rook(BLACK, Square.H8)
                        )
                )
        );
    }
}
