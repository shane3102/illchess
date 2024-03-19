package pl.illchess.domain.board.model.square;

import pl.illchess.domain.board.command.MovePiece;
import pl.illchess.domain.board.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.domain.board.model.history.IsCastling;
import pl.illchess.domain.board.model.history.IsEnPassant;
import pl.illchess.domain.board.model.history.PromotionInfo;
import pl.illchess.domain.board.model.history.Move;
import pl.illchess.domain.board.model.state.FenString;
import pl.illchess.domain.piece.exception.PromotedPieceNotPawnException;
import pl.illchess.domain.piece.exception.PromotedPieceTargetTypeNotSupported;
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
        PromotionInfo promotionInfo = isPromotion(command);

        movePieceMechanic(command, promotionInfo);

        return new Move(
            movedPieceStartSquare,
            command.targetSquare(),
            command.movedPiece(),
            capturedPiece,
            isEnPassant,
            isCastling,
            promotionInfo
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

    private void movePieceMechanic(MovePiece command, PromotionInfo promotionInfo) {
        locations.removeIf(
            piece -> Objects.equals(piece.square(), command.movedPiece().square()) ||
                Objects.equals(piece.square(), command.targetSquare())
        );

        Piece movedPiece = command.movedPiece();

        if (promotionInfo != null) {
            if (!(command.movedPiece() instanceof Pawn pawn)) {
                throw new PromotedPieceNotPawnException();
            }
            if (command.pawnPromotedToPieceType() == null) {
                throw new PromotedPieceTargetTypeNotSupported();
            }
            movedPiece = pawn.promotePawn(command.pawnPromotedToPieceType());
        }

        movedPiece.setSquare(command.targetSquare());

        locations.add(movedPiece);
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

    private PromotionInfo isPromotion(MovePiece command) {
        if (command.targetSquare().getRank().getNumber() == 8 && command.movedPiece() instanceof Pawn && Objects.equals(command.movedPiece().color(), WHITE)) {
            return new PromotionInfo(command.pawnPromotedToPieceType());
        } else if (command.targetSquare().getRank().getNumber() == 1 && command.movedPiece() instanceof Pawn && Objects.equals(command.movedPiece().color(), BLACK)) {
            return new PromotionInfo(command.pawnPromotedToPieceType());
        }
        return null;
    }

    public static PiecesLocations fromFENString(FenString fenPosition) {

        Set<Piece> resultPosition = new HashSet<>();

        char[] files = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        String[] rankLines = fenPosition.value().split(" ")[0].split("/");

        for (int i = 0; i < rankLines.length; i++) {
            int currentRank = 8 - i;
            char[] charArray = rankLines[i].toCharArray();
            int shift = 0;
            for (int j = 0; j < charArray.length; j++) {
                char squareInfo = charArray[j];
                char currentFile = files[j + shift];
                Square currentSquare = Square.fromRankAndFile(currentFile, currentRank);
                switch (squareInfo) {
                    case 'R' -> resultPosition.add(new Rook(WHITE, currentSquare));
                    case 'N' -> resultPosition.add(new Knight(WHITE, currentSquare));
                    case 'B' -> resultPosition.add(new Bishop(WHITE, currentSquare));
                    case 'Q' -> resultPosition.add(new Queen(WHITE, currentSquare));
                    case 'K' -> resultPosition.add(new King(WHITE, currentSquare));
                    case 'P' -> resultPosition.add(new Pawn(WHITE, currentSquare));
                    case 'r' -> resultPosition.add(new Rook(BLACK, currentSquare));
                    case 'n' -> resultPosition.add(new Knight(BLACK, currentSquare));
                    case 'b' -> resultPosition.add(new Bishop(BLACK, currentSquare));
                    case 'q' -> resultPosition.add(new Queen(BLACK, currentSquare));
                    case 'k' -> resultPosition.add(new King(BLACK, currentSquare));
                    case 'p' -> resultPosition.add(new Pawn(BLACK, currentSquare));
                    case '-' -> {
                    }
                    default -> {
                        int currentShift = Integer.parseInt(String.valueOf(squareInfo));
                        shift = shift + currentShift - 1;
                    }
                }
            }
        }

        return new PiecesLocations(resultPosition);
    }

}
