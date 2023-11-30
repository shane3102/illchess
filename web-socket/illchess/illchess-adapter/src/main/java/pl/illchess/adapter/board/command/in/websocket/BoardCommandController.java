package pl.illchess.adapter.board.command.in.websocket;

import lombok.RequiredArgsConstructor;
import pl.illchess.adapter.board.command.in.websocket.request.MovePieceRequest;
import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;

@RequiredArgsConstructor
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final InitializeNewBoardUseCase initializeNewBoardUseCase;

    @Override
    public void movePiece(MovePieceRequest movePieceRequest) {
        movePieceUseCase.movePiece(movePieceRequest.toCmd());
    }

    @Override
    public void initializeNewBoard() {
        initializeNewBoardUseCase.initializeNewGame();
    }
}
