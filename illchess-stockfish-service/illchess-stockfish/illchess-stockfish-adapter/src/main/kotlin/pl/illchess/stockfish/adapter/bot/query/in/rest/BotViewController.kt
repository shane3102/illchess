package pl.illchess.stockfish.adapter.bot.query.`in`.rest

import pl.illchess.stockfish.application.bot.query.out.BotViewQueryPort

class BotViewController(
    private val botViewQueryPort: BotViewQueryPort
) : BotViewApi {

    override fun listAllCurrentlyRunBots() = botViewQueryPort.listAllCurrentlyRunBots()

    override fun getMaxBotCount() = botViewQueryPort.getMaxBotCount()

    override fun getBotExpirationMinutes() = botViewQueryPort.getBotExpirationMinutes()

}