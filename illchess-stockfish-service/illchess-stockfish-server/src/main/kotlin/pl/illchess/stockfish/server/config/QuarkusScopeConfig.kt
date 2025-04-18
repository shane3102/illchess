package pl.illchess.stockfish.server.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty
import pl.illchess.stockfish.application.board.command.out.BotPerformMove
import pl.illchess.stockfish.application.board.command.out.BotQuitNotYetStartedGame
import pl.illchess.stockfish.application.board.command.out.BotResignGame
import pl.illchess.stockfish.application.board.command.out.JoinOrInitializeGame
import pl.illchess.stockfish.application.board.command.out.LoadGame
import pl.illchess.stockfish.application.board.command.out.LoadGameAdditionalInfo
import pl.illchess.stockfish.application.bot.command.BotService
import pl.illchess.stockfish.application.bot.command.out.DeleteBot
import pl.illchess.stockfish.application.bot.command.out.LoadBot
import pl.illchess.stockfish.application.bot.command.out.SaveBot
import pl.illchess.stockfish.application.evaluation.command.EvaluateBoardService
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.CalculateTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.facade.TopMovesFacade
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username

class QuarkusScopeConfig {

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

    @Produces
    @ApplicationScoped
    fun evaluateBoardService(
        loadGame: LoadGame,

        calculateBoardEvaluation: CalculateBoardEvaluation,
        calculateBestMoveAndContinuation: CalculateBestMoveAndContinuation,

        loadBoardEvaluation: LoadBoardEvaluation,
        loadBestMoveAndContinuation: LoadBestMoveAndContinuation,

        saveBoardEvaluation: SaveBoardEvaluation,
        saveBestMoveAndContinuation: SaveBestMoveAndContinuation,

        topMovesFacade: TopMovesFacade

    ): EvaluateBoardService {
        return EvaluateBoardService(
            loadGame,
            calculateBoardEvaluation,
            calculateBestMoveAndContinuation,
            loadBoardEvaluation,
            loadBestMoveAndContinuation,
            saveBoardEvaluation,
            saveBestMoveAndContinuation,
            topMovesFacade
        )
    }

    @Produces
    @ApplicationScoped
    fun botService(
        saveBot: SaveBot,
        loadBot: LoadBot,
        deleteBot: DeleteBot,
        joinOrInitializeGame: JoinOrInitializeGame,
        loadGameAdditionalInfo: LoadGameAdditionalInfo,
        loadGame: LoadGame,
        botPerformMove: BotPerformMove,
        botResignGame: BotResignGame,
        botQuitNotYetStartedGame: BotQuitNotYetStartedGame,
        topMovesFacade: TopMovesFacade
    ) = BotService(
        saveBot,
        loadBot,
        deleteBot,
        joinOrInitializeGame,
        loadGame,
        loadGameAdditionalInfo,
        topMovesFacade,
        botPerformMove,
        botResignGame,
        botQuitNotYetStartedGame,
        maxBotCount.toInt(),
        botExpirationMinutes.toLong()
    )

    @Produces
    @ApplicationScoped
    fun topMovesFacade(
        loadTopMoves: LoadTopMoves,
        saveTopMoves: SaveTopMoves,
        calculateTopMoves: CalculateTopMoves
    ) = TopMovesFacade(
        loadTopMoves, saveTopMoves, calculateTopMoves
    )

    @Produces
    @ApplicationScoped
    fun botCache(): HashMap<Username, Bot> = hashMapOf()

}