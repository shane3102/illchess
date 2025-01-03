package pl.illchess.stockfish.domain.bot.exception

import pl.illchess.stockfish.domain.bot.domain.Username
import pl.illchess.stockfish.domain.commons.NotFoundException

class BotNotFound(val username: Username) :
    NotFoundException("Bot with username = ${username.text} not found")