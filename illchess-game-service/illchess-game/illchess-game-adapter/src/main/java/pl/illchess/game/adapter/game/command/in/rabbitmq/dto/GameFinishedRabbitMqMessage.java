package pl.illchess.game.adapter.game.command.in.rabbitmq.dto;

import java.util.UUID;

public record GameFinishedRabbitMqMessage(UUID id) {
}
