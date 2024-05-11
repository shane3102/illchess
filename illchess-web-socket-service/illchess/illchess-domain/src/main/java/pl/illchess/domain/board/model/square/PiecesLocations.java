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

    public Move movePiece(MovePiece command, Piece movedPiece, Move lastPerformedMove) {
        Square movedPieceStartSquare = command.startSquare();

        IsEnPassant isEnPassant = new IsEnPassant(false);
        Piece capturedPiece = null;

        if (movedPiece instanceof Pawn pawn) {
            Square square = pawn.getSquareOfPieceCapturedEnPassant(command.targetSquare(), lastPerformedMove);
            if (square != null) {
                Optional<Piece> foundPiece = findPieceOnSquare(square);

                if (foundPiece.isPresent() && foundPiece.get() instanceof Pawn) {
                    isEnPassant = new IsEnPassant(true);
                    capturedPiece = foundPiece.get();
                    locations.removeIf(piece ->
                        Objects.equals(piece.square(), square)
                    );
                }
            }
        }

        capturedPiece = capturedPiece != null ? capturedPiece : findPieceOnSquare(command.targetSquare()).orElse(null);
        IsCastling isCastling = moveRookIfCastlingMove(movedPiece, command);
        PromotionInfo promotionInfo = isPromotion(movedPiece, command);

        movePieceMechanic(command, movedPiece, promotionInfo);

        return new Move(
            movedPieceStartSquare,
            command.targetSquare(),
            movedPiece,
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

    private void movePieceMechanic(MovePiece command, Piece movedPiece, PromotionInfo promotionInfo) {

        Square movedPieceSquare = movedPiece.square();

        locations.removeIf(
            piece -> Objects.equals(piece.square(), movedPieceSquare) ||
                Objects.equals(piece.square(), command.targetSquare())
        );


        if (promotionInfo != null) {
            if (!(movedPiece instanceof Pawn pawn)) {
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

    private IsCastling moveRookIfCastlingMove(Piece movedPiece, MovePiece command) {
        IsCastling isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.G1, Square.E1, Square.H1, Square.F1);
        if (isCastling.value()) {
            return isCastling;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.C1, Square.E1, Square.A1, Square.D1);
        if (isCastling.value()) {
            return isCastling;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.G8, Square.E8, Square.H8, Square.F8);
        if (isCastling.value()) {
            return isCastling;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.C8, Square.E8, Square.A8, Square.D8);
        return isCastling;
    }

    private IsCastling moveRookIfCastlingMoveByCorner(
        MovePiece command,
        Piece movedPiece,
        Square targetSquare,
        Square kingSquare,
        Square expectedRookSquare,
        Square rookSquareAfterEnPassant
    ) {
        if (movedPiece instanceof King king && command.targetSquare().equals(targetSquare) && king.square().equals(kingSquare)) {
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

    private PromotionInfo isPromotion(Piece movedPiece, MovePiece command) {
        if (command.targetSquare().getRank().getNumber() == 8 && movedPiece instanceof Pawn && Objects.equals(movedPiece.color(), WHITE)) {
            return new PromotionInfo(command.pawnPromotedToPieceType());
        } else if (command.targetSquare().getRank().getNumber() == 1 && movedPiece instanceof Pawn && Objects.equals(movedPiece.color(), BLACK)) {
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