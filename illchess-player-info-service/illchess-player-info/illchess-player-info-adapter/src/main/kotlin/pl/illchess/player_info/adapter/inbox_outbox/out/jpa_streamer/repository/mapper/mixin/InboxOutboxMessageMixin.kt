package pl.illchess.player_info.adapter.inbox_outbox.out.jpa_streamer.repository.mapper.mixin

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.illchess.player_info.adapter.game.command.`in`.inbox.dto.ObtainNewGameInboxMessage

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = ObtainNewGameInboxMessage::class, name = "ObtainNewGameInboxMessage")
    ]
)
class InboxOutboxMessageMixin