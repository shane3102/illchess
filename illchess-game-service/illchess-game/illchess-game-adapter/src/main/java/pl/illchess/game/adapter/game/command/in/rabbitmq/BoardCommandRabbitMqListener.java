package pl.illchess.game.adapter.game.command.in.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.illchess.game.adapter.game.command.in.inbox.dto.GameFinishedInboxMessage;
import pl.illchess.game.adapter.game.command.in.rabbitmq.dto.GameFinishedRabbitMqMessage;
import pl.illchess.game.adapter.game.command.in.rabbitmq.dto.GameObtainingErrorRabbitMqMessage;
import pl.illchess.game.application.game.command.in.DeleteFinishedGameUseCase;
import pl.illchess.game.application.game.command.in.DeleteFinishedGameUseCase.DeleteBoardWithFinishedGameCmd;
import pl.illchess.game.application.commons.command.out.PublishEvent;
import pl.illchess.game.domain.game.event.delete.GameDeleteError;
import pl.illchess.game.domain.game.event.delete.GameDeleted;
import pl.illchess.game.domain.game.model.GameId;
import pl.illchess.game.domain.commons.exception.DomainException;
import pl.shane3102.messaging.aggregator.InboxOutbox;

@Component
@RequiredArgsConstructor
public class BoardCommandRabbitMqListener {

    public static final String OBTAIN_GAME_SUCCESS_QUEUE = "obtain-game-success";
    public static final String OBTAIN_GAME_FAILURE_QUEUE = "obtain-game-failure";

    private final InboxOutbox inboxOutbox;
    private final PublishEvent publishEvent;
    private final DeleteFinishedGameUseCase deleteFinishedGameUseCase;

    @RabbitListener(queues = {OBTAIN_GAME_SUCCESS_QUEUE})
    public void deleteBoardWithFinishedGameOrSaveAsInboxMessage(GameFinishedRabbitMqMessage gameFinishedRabbitMqMessage) {
        try {
            DeleteBoardWithFinishedGameCmd cmd = new DeleteBoardWithFinishedGameCmd(gameFinishedRabbitMqMessage.id());
            deleteFinishedGameUseCase.deleteBoardWithFinishedGame(cmd);
            publishEvent.publishDomainEvent(new GameDeleted(new GameId(cmd.gameId())));
        }  catch (DomainException domainException) {
            inboxOutbox.saveMessage(new GameFinishedInboxMessage(gameFinishedRabbitMqMessage.id()));
        }
    }

    @RabbitListener(queues = {OBTAIN_GAME_FAILURE_QUEUE})
    public void publishEventOfErrorWhenObtainingGame(GameObtainingErrorRabbitMqMessage message) {
        publishEvent.publishDomainEvent(new GameDeleteError(new GameId(message.id())));
    }
}
