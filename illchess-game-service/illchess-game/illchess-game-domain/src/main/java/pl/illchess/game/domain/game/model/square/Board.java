package pl.illchess.game.domain.game.model.square;

import pl.illchess.game.domain.game.command.MovePiece;
import pl.illchess.game.domain.game.exception.BoardFenPositionCouldNotBeEstablishedException;
import pl.illchess.game.domain.game.exception.PieceNotPresentOnGivenSquare;
import pl.illchess.game.domain.game.model.FenBoardString;
import pl.illchess.game.domain.game.model.history.IsCastling;
import pl.illchess.game.domain.game.model.history.Move;
import pl.illchess.game.domain.game.model.history.PromotionInfo;
import pl.illchess.game.domain.game.model.square.info.SquareFile;
import pl.illchess.game.domain.game.model.square.info.SquareRank;
import pl.illchess.game.domain.game.model.state.player.PreMove;
import pl.illchess.game.domain.piece.exception.PieceTypeNotRecognisedException;
import pl.illchess.game.domain.piece.exception.PromotedPieceNotPawnException;
import pl.illchess.game.domain.piece.exception.PromotedPieceTargetTypeNotSupported;
import pl.illchess.game.domain.piece.model.Piece;
import pl.illchess.game.domain.piece.model.PieceCapableOfPinning;
import pl.illchess.game.domain.piece.model.info.PieceColor;
import pl.illchess.game.domain.piece.model.type.Bishop;
import pl.illchess.game.domain.piece.model.type.King;
import pl.illchess.game.domain.piece.model.type.Knight;
import pl.illchess.game.domain.piece.model.type.Pawn;
import pl.illchess.game.domain.piece.model.type.Queen;
import pl.illchess.game.domain.piece.model.type.Rook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.illchess.game.domain.piece.model.info.PieceColor.BLACK;
import static pl.illchess.game.domain.piece.model.info.PieceColor.WHITE;

public record Board(
    Set<Piece> piecesLocations
) {

    public Move movePiece(
        MovePiece command,
        Piece movedPiece,
        Move lastPerformedMove,
        FenBoardString fenStringBeforeMove
    ) {
        Square movedPieceStartSquare = command.startSquare();

        Piece capturedPiece = null;

        if (movedPiece instanceof Pawn pawn) {
            Square square = pawn.getSquareOfPieceCapturedEnPassant(command.targetSquare(), lastPerformedMove);
            if (square != null) {
                Optional<Piece> foundPiece = findPieceOnSquare(square);

                if (foundPiece.isPresent() && foundPiece.get() instanceof Pawn) {
                    capturedPiece = foundPiece.get();
                    piecesLocations.removeIf(piece ->
                        Objects.equals(piece.square(), square)
                    );
                }
            }
        }

        capturedPiece = capturedPiece != null ? capturedPiece : findPieceOnSquare(command.targetSquare()).orElse(null);
        moveRookIfCastlingMove(movedPiece, command);
        PromotionInfo promotionInfo = isPromotion(movedPiece, command);

        movePieceMechanic(command, movedPiece, promotionInfo);

        return new Move(
            movedPieceStartSquare,
            command.targetSquare(),
            movedPiece,
            capturedPiece,
            fenStringBeforeMove
        );
    }

    public PreMove movePieceOnPreMove(
        MovePiece command,
        Piece movedPiece
    ) {
        PromotionInfo promotionInfo = isPromotion(movedPiece, command);
        movePieceMechanic(command, movedPiece, promotionInfo);
        return new PreMove(
            command.startSquare(),
            command.targetSquare(),
            command.pawnPromotedToPieceType(),
            this
        );
    }

    public Optional<Piece> getPieceByTypeAndColor(Class<? extends Piece> pieceType, PieceColor pieceColor) {
        return piecesLocations.stream()
            .filter(piece ->
                Objects.equals(piece.getClass(), pieceType)
                    && Objects.equals(piece.color(), pieceColor)
            )
            .findFirst();
    }

    public Optional<Piece> findPieceOnSquare(Square square) {
        return piecesLocations.stream()
            .filter(piece -> Objects.equals(piece.square(), square))
            .findFirst();
    }

    public Set<Piece> getEnemyPieces(PieceColor color) {
        return piecesLocations.stream()
            .filter(piece -> !Objects.equals(piece.color(), color))
            .collect(Collectors.toSet());
    }

    public Set<Piece> getAlliedPieces(PieceColor color) {
        return piecesLocations.stream()
            .filter(piece -> Objects.equals(piece.color(), color))
            .collect(Collectors.toSet());
    }

    public Set<PieceCapableOfPinning> getEnemyPiecesCapableOfPinning(PieceColor color) {
        return piecesLocations.stream()
            .filter(piece -> !Objects.equals(piece.color(), color))
            .filter(piece -> piece instanceof PieceCapableOfPinning)
            .map(piece -> (PieceCapableOfPinning) piece)
            .collect(Collectors.toSet());
    }

    private void movePieceMechanic(MovePiece command, Piece movedPiece, PromotionInfo promotionInfo) {

        Square movedPieceSquare = movedPiece.square();

        piecesLocations.removeIf(
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

        piecesLocations.add(movedPiece);
    }

    private void moveRookIfCastlingMove(Piece movedPiece, MovePiece command) {
        IsCastling isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.G1, Square.E1, Square.H1, Square.F1);
        if (isCastling.value()) {
            return;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.C1, Square.E1, Square.A1, Square.D1);
        if (isCastling.value()) {
            return;
        }
        isCastling = moveRookIfCastlingMoveByCorner(command, movedPiece, Square.G8, Square.E8, Square.H8, Square.F8);
        if (isCastling.value()) {
            return;
        }
        moveRookIfCastlingMoveByCorner(command, movedPiece, Square.C8, Square.E8, Square.A8, Square.D8);
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
            Rook foundRook = piecesLocations.stream()
                .filter(it -> it.square().equals(expectedRookSquare))
                .filter(it -> it instanceof Rook)
                .findFirst()
                .map(it -> (Rook) it)
                .orElseThrow(() -> new PieceNotPresentOnGivenSquare(command.gameId(), Rook.class, expectedRookSquare));

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

    public void applyPositionByFenString(FenBoardString fenBoardString) {
        piecesLocations.clear();
        piecesLocations.addAll(getPositionByFenString(fenBoardString));
    }

    public static Board fromFENString(FenBoardString fenPosition) {

        Set<Piece> resultPosition = getPositionByFenString(fenPosition);

        return new Board(resultPosition);
    }

    private static Set<Piece> getPositionByFenString(FenBoardString fenPosition) {
        Set<Piece> resultPosition = new HashSet<>();

        char[] files = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        String[] rankLines = fenPosition.position().split("/");

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
        return resultPosition;
    }

    public String establishFenPosition() {

        List<SquareRank> ranks = Arrays.stream(SquareRank.values()).collect(Collectors.toList());
        Collections.reverse(ranks);

        return ranks.stream()
            .map(rank -> {

                StringBuilder result = new StringBuilder();

                Map<SquareFile, Piece> piecesOnRankByFile = piecesLocations.stream()
                    .filter(piece -> piece.square().rank.equals(rank))
                    .collect(Collectors.toMap(
                        piece -> piece.square().file,
                        piece -> piece
                    ));

                int betweenPieces = 0;
                for (SquareFile file : SquareFile.values()) {
                    if (piecesOnRankByFile.get(file) != null) {
                        if (betweenPieces > 0) {
                            result.append(betweenPieces);
                        }
                        betweenPieces = 0;
                        result.append(getFenPieceCharacter(piecesOnRankByFile.get(file)));
                    } else {
                        betweenPieces++;
                    }
                }
                if (betweenPieces > 0) {
                    result.append(betweenPieces);
                }

                return result.toString();
            })
            .reduce((acc, val) -> {
                acc += '/';
                acc += val;
                return acc;
            })
            .orElseThrow(BoardFenPositionCouldNotBeEstablishedException::new);
    }

    public Board cloneBoard() {
        return new Board(piecesLocations.stream().map(Piece::clonePiece).collect(Collectors.toSet()));
    }

    private String getFenPieceCharacter(Piece piece) {
        if (piece instanceof Pawn) {
            return piece.color() == WHITE ? "P" : "p";
        } else if (piece instanceof Bishop) {
            return piece.color() == WHITE ? "B" : "b";
        } else if (piece instanceof Rook) {
            return piece.color() == WHITE ? "R" : "r";
        } else if (piece instanceof Knight) {
            return piece.color() == WHITE ? "N" : "n";
        } else if (piece instanceof Queen) {
            return piece.color() == WHITE ? "Q" : "q";
        } else if (piece instanceof King) {
            return piece.color() == WHITE ? "K" : "k";
        } else throw new PieceTypeNotRecognisedException(piece.getClass());
    }
}
