package pl.illchess.game.adapter.board.command.in.rabbitmq.dto;

import java.util.UUID;

public record GameFinishedRabbitMqMessage(UUID id) {
}