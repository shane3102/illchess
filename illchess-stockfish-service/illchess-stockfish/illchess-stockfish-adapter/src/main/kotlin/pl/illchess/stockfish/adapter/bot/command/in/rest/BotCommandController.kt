package pl.illchess.stockfish.adapter.bot.command.`in`.rest

import org.jboss.resteasy.reactive.RestResponse
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.AddBotRequest
import pl.illchess.stockfish.adapter.bot.command.`in`.rest.dto.DeleteBotRequest
import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase
import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase

class BotCommandController(
    private val addBotsUseCase: AddBotsUseCase,
    private val deleteBotsUseCase: DeleteBotsUseCase
) : BotCommandApi {

    override fun deleteBots(request: DeleteBotRequest): RestResponse<Void> {
        deleteBotsUseCase.deleteBots(request.toCmd())
        return RestResponse.ok()
    }

    override fun addBots(request: AddBotRequest): RestResponse<Void> {
        addBotsUseCase.addBots(request.toCmd())
        return RestResponse.ok()
    }

}