package pl.illchess.stockfish.domain.bot.exception

import pl.illchess.stockfish.domain.commons.BadRequestException

class TooManyBotsAddedException(maxBotCount: Int): BadRequestException("Too maxy bots added. Max allowed bots count id $maxBotCount")