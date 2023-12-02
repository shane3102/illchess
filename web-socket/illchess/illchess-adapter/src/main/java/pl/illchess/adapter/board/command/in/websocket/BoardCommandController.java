package pl.illchess.adapter.board.command.in.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.illchess.adapter.board.command.in.websocket.request.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.request.MovePieceRequest;
import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BoardCommandController implements BoardCommandApi {

    private final MovePieceUseCase movePieceUseCase;
    private final InitializeNewBoardUseCase initializeNewBoardUseCase;

    @Override
    @MessageMapping("/board/move-piece")
    public void movePiece(MovePieceRequest movePieceRequest) {
        movePieceUseCase.movePiece(movePieceRequest.toCmd());
    }

    @Override
    @MessageMapping("/board/create")
    public void initializeNewBoard(InitializeNewBoardRequest initializeNewBoardRequest) {
        initializeNewBoardUseCase.initializeNewGame(initializeNewBoardRequest.toCmd());
    }
}
