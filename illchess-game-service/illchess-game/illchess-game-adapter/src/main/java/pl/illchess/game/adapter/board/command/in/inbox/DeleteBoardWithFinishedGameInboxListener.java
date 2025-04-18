package pl.illchess.game.adapter.board.command.in.inbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.illchess.game.adapter.board.command.in.inbox.dto.GameFinishedInboxMessage;
import pl.illchess.game.application.game.command.in.DeleteFinishedGameUseCase;
import pl.illchess.game.application.game.command.in.DeleteFinishedGameUseCase.DeleteBoardWithFinishedGameCmd;
import pl.shane3102.messaging.annotation.InboxOutboxListener;
import pl.shane3102.messaging.annotation.MessagingAwareComponent;

@Component
@MessagingAwareComponent
@RequiredArgsConstructor
public class DeleteBoardWithFinishedGameInboxListener {

    private final DeleteFinishedGameUseCase deleteFinishedGameUseCase;

    @InboxOutboxListener(
        retryCount = 100,
        batchSize = 100,
        cron = "0 0 * * * *"
    )
    void deleteBoardWithFinishedGame(GameFinishedInboxMessage message) {
        deleteFinishedGameUseCase.deleteBoardWithFinishedGame(new DeleteBoardWithFinishedGameCmd(message.getBoardId()));
    }
}
