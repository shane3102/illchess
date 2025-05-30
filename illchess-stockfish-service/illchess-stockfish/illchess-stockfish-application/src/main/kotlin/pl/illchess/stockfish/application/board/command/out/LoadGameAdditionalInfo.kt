package pl.illchess.stockfish.application.board.command.out

import pl.illchess.stockfish.domain.board.domain.GameAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.GameId

interface LoadGameAdditionalInfo {
    fun loadGameAdditionalInfo(gameId: GameId): GameAdditionalInfo?
}