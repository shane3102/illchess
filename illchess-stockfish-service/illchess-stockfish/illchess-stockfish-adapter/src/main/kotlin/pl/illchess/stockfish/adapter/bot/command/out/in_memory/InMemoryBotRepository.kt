package pl.illchess.stockfish.adapter.bot.command.out.in_memory

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.application.bot.command.out.DeleteBot
import pl.illchess.stockfish.application.bot.command.out.LoadBot
import pl.illchess.stockfish.application.bot.command.out.SaveBot
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

@ApplicationScoped
class InMemoryBotRepository(
    private val botCache: HashMap<Username, Bot>
) : LoadBot, SaveBot, DeleteBot {

    override fun loadBot(username: Username) = botCache[username]

    override fun saveBot(bot: Bot) {
        botCache[bot.username] = bot
    }

    override fun deleteBot(username: Username) {
        botCache.remove(username)
    }
}