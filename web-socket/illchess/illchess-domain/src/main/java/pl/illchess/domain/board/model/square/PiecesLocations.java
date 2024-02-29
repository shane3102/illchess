package pl.illchess.domain.board.model.square;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.model.history.IsCastling;
import pl.illchess.domain.board.model.history.IsEnPassant;
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

        Piece capturedPiece = findPieceOnSquare(command.targetSquare()).orElse(null);

        IsEnPassant isEnPassant = removePieceIfEnPassantMove(command, lastPerformedMove);
        IsCastling isCastling = moveRookIfCastlingMove(command);

        movePieceMechanic(command);

        return new Move(
            movedPieceStartSquare,
            command.targetSquare(),
            command.movedPiece(),
            capturedPiece,
            isEnPassant,
            isCastling
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
            // TODO co z en passant/roszadą ???
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

    public Optional<Piece> findPieceOnSquare(Square square) {
        return locations.stream()
            .filter(piece -> Objects.equals(piece.square(), square))
            .findFirst();
    }

    public Set<Piece> getEnemyPieces(PieceColor color) {
        return locations.stream()
            .filter(piece -> !Objects.equals(piece.color(), color))
            .collect(Collectors.toSet());
    }

    public Set<Piece> getAlliedPieces(PieceColor color) {
        return locations.stream()
            .filter(piece -> Objects.equals(piece.color(), color))
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
        return Objects.equals(findPieceOnSquare(command.movedPiece().square()).orElse(null), command.movedPiece());
    }

    private void movePieceMechanic(MovePiece command) {
        locations.removeIf(
            piece -> Objects.equals(piece.square(), command.movedPiece().square()) ||
                Objects.equals(piece.square(), command.targetSquare())
        );

        command.movedPiece().setSquare(command.targetSquare());

        locations.add(command.movedPiece());
    }

    private IsEnPassant removePieceIfEnPassantMove(MovePiece command, Move lastPerformedMove) {
        if (command.movedPiece() instanceof Pawn pawn) {
            Square square = pawn.getSquareOfPieceCapturedEnPassant(command.targetSquare(), lastPerformedMove);
            if (square != null) {
                boolean wasPawnRemoved = locations.removeIf(piece -> Objects.equals(piece.square(), square));
                return new IsEnPassant(wasPawnRemoved);
            }
        }
        return new IsEnPassant(false);
    }

    private IsCastling moveRookIfCastlingMove(MovePiece command) {
        IsCastling isCastling = moveRookIfCastlingMoveByCorner(command, Square.G1, Square.E1, Square.H1, Square.F1);
        if (isCastling.value()) {
            return isCastling;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, Square.C1, Square.E1, Square.A1, Square.D1);
        if (isCastling.value()) {
            return isCastling;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, Square.G8, Square.E8, Square.H8, Square.F8);
        if (isCastling.value()) {
            return isCastling;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, Square.C8, Square.E8, Square.A8, Square.D8);
        return isCastling;
    }

    private IsCastling moveRookIfCastlingMoveByCorner(
        MovePiece command,
        Square targetSquare,
        Square kingSquare,
        Square expectedRookSquare,
        Square rookSquareAfterEnPassant
    ) {
        if (command.movedPiece() instanceof King king && command.targetSquare().equals(targetSquare) && king.square().equals(kingSquare)) {
            Rook foundRook = locations.stream()
                .filter(it -> it.square().equals(expectedRookSquare))
                .filter(it -> it instanceof Rook)
                .findFirst()
                .map(it -> (Rook) it)
                .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.boardId(), Rook.class, expectedRookSquare));

            foundRook.setSquare(rookSquareAfterEnPassant);
            return IsCastling.yep();
        }
        return IsCastling.nope();
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

                    new Pawn(WHITE, Square.A2),
                    new Pawn(WHITE, Square.B2),
                    new Pawn(WHITE, Square.C2),
                    new Pawn(WHITE, Square.D2),
                    new Pawn(WHITE, Square.E2),
                    new Pawn(WHITE, Square.F2),
                    new Pawn(WHITE, Square.G2),
                    new Pawn(WHITE, Square.H2),

                    new Pawn(BLACK, Square.A7),
                    new Pawn(BLACK, Square.B7),
                    new Pawn(BLACK, Square.C7),
                    new Pawn(BLACK, Square.D7),
                    new Pawn(BLACK, Square.E7),
                    new Pawn(BLACK, Square.F7),
                    new Pawn(BLACK, Square.G7),
                    new Pawn(BLACK, Square.H7),

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
