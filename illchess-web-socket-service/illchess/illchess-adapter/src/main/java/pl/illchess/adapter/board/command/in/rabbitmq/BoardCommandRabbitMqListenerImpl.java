package pl.illchess.adapter.board.command.in.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.illchess.adapter.board.command.in.inbox.dto.GameFinishedInboxMessage;
import pl.illchess.adapter.board.command.in.rabbitmq.dto.GameFinishedRabbitMqMessage;
import pl.illchess.application.board.command.in.DeleteBoardWithFinishedGameUseCase;
import pl.illchess.application.board.command.in.DeleteBoardWithFinishedGameUseCase.DeleteBoardWithFinishedGameCmd;
import pl.illchess.domain.commons.exception.DomainException;
import pl.messaging.aggregator.InboxOutbox;

@Component
@RequiredArgsConstructor
public class BoardCommandRabbitMqListenerImpl {

    public static final String OBTAIN_GAME_SUCCESS_QUEUE = "obtain-game-success";

    private final InboxOutbox inboxOutbox;
    private final DeleteBoardWithFinishedGameUseCase deleteBoardWithFinishedGameUseCase;

    @RabbitListener(queues = {OBTAIN_GAME_SUCCESS_QUEUE})
    public void deleteBoardWithFinishedGameOrSaveAsInboxMessage(GameFinishedRabbitMqMessage gameFinishedRabbitMqMessage) {
        try {
            DeleteBoardWithFinishedGameCmd cmd = new DeleteBoardWithFinishedGameCmd(gameFinishedRabbitMqMessage.id());
            deleteBoardWithFinishedGameUseCase.deleteBoardWithFinishedGame(cmd);
        }  catch (DomainException domainException) {
            inboxOutbox.saveMessage(new GameFinishedInboxMessage(gameFinishedRabbitMqMessage.id()));
        }
    }
}
