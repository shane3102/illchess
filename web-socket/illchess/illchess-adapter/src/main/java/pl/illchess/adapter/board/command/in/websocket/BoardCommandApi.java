package pl.illchess.adapter.board.command.in.websocket;

import org.springframework.messaging.handler.annotation.Payload;
import pl.illchess.adapter.board.command.in.websocket.request.InitializeNewBoardRequest;
import pl.illchess.adapter.board.command.in.websocket.request.MovePieceRequest;
import pl.illchess.domain.board.event.BoardPiecesLocationsUpdated;

public interface BoardCommandApi {

    void movePiece(@Payload MovePieceRequest movePieceRequest);

    void initializeNewBoard(@Payload InitializeNewBoardRequest initializeNewBoardRequest);

    void checkGameState(BoardPiecesLocationsUpdated boardUpdated);

}
