package pl.illchess.adapter.board.command.in.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import pl.illchess.adapter.board.command.in.websocket.request.MovePieceRequest;

@Controller
public interface BoardCommandApi {

    @MessageMapping("/board/move-piece")
//    @SendTo("/chess-topic/public")
    void movePiece(@Payload MovePieceRequest movePieceRequest);

    @MessageMapping("/board/create")
//    @SendTo("/chess-topic/public")
    void initializeNewBoard();
}
