package pl.illchess.adapter.board.command.in.inbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.illchess.adapter.board.command.in.inbox.dto.GameFinishedInboxMessage;
import pl.illchess.application.board.command.in.DeleteBoardWithFinishedGameUseCase;
import pl.illchess.application.board.command.in.DeleteBoardWithFinishedGameUseCase.DeleteBoardWithFinishedGameCmd;
import pl.messaging.annotation.InboxOutboxListener;
import pl.messaging.annotation.MessagingAwareComponent;

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
