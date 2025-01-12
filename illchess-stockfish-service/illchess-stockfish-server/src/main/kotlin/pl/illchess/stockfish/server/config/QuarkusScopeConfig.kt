package pl.illchess.stockfish.server.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.application.bot.command.BotService
import pl.illchess.stockfish.application.bot.command.out.DeleteBot
import pl.illchess.stockfish.application.bot.command.out.LoadBot
import pl.illchess.stockfish.application.bot.command.out.SaveBot
import pl.illchess.stockfish.application.evaluation.command.EvaluateBoardService
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.board.command.out.BotPerformMove
import pl.illchess.stockfish.application.board.command.out.BotQuitNotYetStartedGame
import pl.illchess.stockfish.application.board.command.out.BotResignGame
import pl.illchess.stockfish.application.board.command.out.JoinOrInitializeBoard
import pl.illchess.stockfish.application.board.command.out.LoadBoard
import pl.illchess.stockfish.application.board.command.out.LoadBoardAdditionalInfo
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

class QuarkusScopeConfig {

    @field:ConfigProperty(
        name = "bots.max-count",
        defaultValue = "8"
    )
    lateinit var maxBotCount: String

    @field:ConfigProperty(
        name = "bots.expiration-minutes",
        defaultValue = "5"
    )
    lateinit var botExpirationMinutes: String

    @Produces
    @ApplicationScoped
    fun evaluateBoardService(
        loadBoardEvaluation: LoadBoardEvaluation,
        loadBoard: LoadBoard,
        loadBestMoveAndContinuation: LoadBestMoveAndContinuation,
        loadTopMoves: LoadTopMoves
    ): EvaluateBoardService {
        return EvaluateBoardService(loadBoard, loadBoardEvaluation, loadBestMoveAndContinuation, loadTopMoves)
    }

    @Produces
    @ApplicationScoped
    fun botService(
        saveBot: SaveBot,
        loadBot: LoadBot,
        deleteBot: DeleteBot,
        joinOrInitializeBoard: JoinOrInitializeBoard,
        loadBoardAdditionalInfo: LoadBoardAdditionalInfo,
        loadBoard: LoadBoard,
        loadTopMoves: LoadTopMoves,
        botPerformMove: BotPerformMove,
        botResignGame: BotResignGame,
        botQuitNotYetStartedGame: BotQuitNotYetStartedGame,
    ) = BotService(
        saveBot,
        loadBot,
        deleteBot,
        joinOrInitializeBoard,
        loadBoard,
        loadBoardAdditionalInfo,
        loadTopMoves,
        botPerformMove,
        botResignGame,
        botQuitNotYetStartedGame,
        maxBotCount.toInt(),
        botExpirationMinutes.toLong()
    )

    @Produces
    @ApplicationScoped
    fun botCache(): HashMap<Username, Bot> = hashMapOf()
}