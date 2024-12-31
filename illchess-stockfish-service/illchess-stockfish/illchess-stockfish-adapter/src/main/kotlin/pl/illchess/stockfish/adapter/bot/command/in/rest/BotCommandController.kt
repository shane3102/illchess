package pl.illchess.stockfish.adapter.bot.command.`in`.rest

import org.jboss.resteasy.reactive.RestResponse
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.AddBotsRequest
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.DeleteBotsRequest
import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase
import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase

class BotCommandController(
    private val addBotsUseCase: AddBotsUseCase,
    private val deleteBotsUseCase: DeleteBotsUseCase
) : BotCommandApi {

    override fun deleteBots(request: DeleteBotsRequest): RestResponse<Void> {
        deleteBotsUseCase.deleteBots(request.toCmd())
        return RestResponse.ok()
    }

    override fun addBots(request: AddBotsRequest): RestResponse<Void> {
        addBotsUseCase.addBots(request.toCmd())
        return RestResponse.ok()
    }

}