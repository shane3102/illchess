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
        defaultValue = "8"
    )
    lateinit var maxBotCount: String

    override fun listAllCurrentlyRunBots() = botCache.values
        .map {
            BotView(it.username.text, it.currentBoardId?.uuid)
        }

    override fun getMaxBotCount(): Int {
        return maxBotCount.toInt()
    }

}