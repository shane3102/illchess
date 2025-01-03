package pl.illchess.stockfish.domain.bot.command

import pl.illchess.stockfish.domain.bot.domain.Username

data class DeleteBots(val usernames: List<Username>)
