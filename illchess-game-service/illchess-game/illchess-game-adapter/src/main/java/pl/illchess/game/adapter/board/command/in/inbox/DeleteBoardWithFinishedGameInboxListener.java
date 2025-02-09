package pl.illchess.game.adapter.board.command.in.inbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.illchess.game.adapter.board.command.in.inbox.dto.GameFinishedInboxMessage;
import pl.illchess.game.application.board.command.in.DeleteBoardWithFinishedGameUseCase;
import pl.illchess.game.application.board.command.in.DeleteBoardWithFinishedGameUseCase.DeleteBoardWithFinishedGameCmd;
import pl.shane3102.messaging.annotation.InboxOutboxListener;
import pl.shane3102.messaging.annotation.MessagingAwareComponent;

@Component
@MessagingAwareComponent
@RequiredArgsConstructor
public class DeleteBoardWithFinishedGameInboxListener {

    private final DeleteBoardWithFinishedGameUseCase deleteBoardWithFinishedGameUseCase;

    @InboxOutboxListener(
        retryCount = 100,
        batchSize = 100,
        cron = "0 0 * * * *"
    )
    void deleteBoardWithFinishedGame(GameFinishedInboxMessage message) {
        deleteBoardWithFinishedGameUseCase.deleteBoardWithFinishedGame(new DeleteBoardWithFinishedGameCmd(message.getBoardId()));
    }
}
