package pl.illchess.stockfish.adapter.bot.query.out.in_memory

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.application.bot.query.out.BotViewQueryPort
import pl.illchess.stockfish.application.bot.query.out.model.BotView
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

@ApplicationScoped
class InMemoryBotViewRepository(
    private val botCache: HashMap<Username, Bot>
) : BotViewQueryPort {

    @field:ConfigProperty(
        name = "bots.max-count",
        defaultValue = "12"
    )
    lateinit var maxBotCount: String

    @field:ConfigProperty(
        name = "bots.expiration-minutes",
        defaultValue = "5"
    )
    lateinit var botExpirationMinutes: String

    override fun listAllCurrentlyRunBots() = botCache.values
        .sortedBy { it.expirationDate }
        .map {
            BotView(it.username.text, it.currentGameId?.uuid)
        }

    override fun getMaxBotCount(): Int {
        return maxBotCount.toInt()
    }

    override fun getBotExpirationMinutes(): Long {
        return botExpirationMinutes.toLong()
    }

}