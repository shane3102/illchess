package pl.illchess.stockfish.application.bot.command

import java.time.LocalDateTime
import kotlin.concurrent.thread
import kotlin.random.Random
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.stockfish.application.board.command.out.BotPerformMove
import pl.illchess.stockfish.application.board.command.out.BotQuitNotYetStartedGame
import pl.illchess.stockfish.application.board.command.out.BotResignGame
import pl.illchess.stockfish.application.board.command.out.JoinOrInitializeGame
import pl.illchess.stockfish.application.board.command.out.LoadGame
import pl.illchess.stockfish.application.board.command.out.LoadGameAdditionalInfo
import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase
import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase
import pl.illchess.stockfish.application.bot.command.`in`.DeleteExpiredBotsUseCase
import pl.illchess.stockfish.application.bot.command.out.DeleteBot
import pl.illchess.stockfish.application.bot.command.out.LoadBot
import pl.illchess.stockfish.application.bot.command.out.SaveBot
import pl.illchess.stockfish.application.evaluation.command.out.facade.TopMovesFacade
import pl.illchess.stockfish.domain.board.domain.BoardAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.board.exception.BoardNotFoundException
import pl.illchess.stockfish.domain.bot.command.PerformMove
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username
import pl.illchess.stockfish.domain.bot.exception.BotNotFound
import pl.illchess.stockfish.domain.bot.exception.TooManyBotsAddedException
import pl.illchess.stockfish.domain.evaluation.command.EstablishListOfTopMoves
import pl.illchess.stockfish.domain.evaluation.exception.TopMovesCouldNotBeEstablished

class BotService(
    private val saveBot: SaveBot,
    private val loadBot: LoadBot,
    private val deleteBot: DeleteBot,
    private val joinOrInitializeGame: JoinOrInitializeGame,
    private val loadGame: LoadGame,
    private val loadGameAdditionalInfo: LoadGameAdditionalInfo,
    private val topMovesFacade: TopMovesFacade,
    private val botPerformMove: BotPerformMove,
    private val botResignGame: BotResignGame,
    private val botQuitNotYetStartedGame: BotQuitNotYetStartedGame,
    private val botsMaxCount: Int,
    private val botExpirationMinutes: Long
) : AddBotsUseCase, DeleteBotsUseCase, DeleteExpiredBotsUseCase {

    override fun addBots(cmd: AddBotsUseCase.AddBotsCmd) {
        val addedBotCount = cmd.addedBotCmd.count()
        log.info("Adding $addedBotCount bots with usernames: ${cmd.addedBotCmd.map { it.username }}")
        val command = cmd.toCommand(botExpirationMinutes)
        if (loadBot.botCount() + addedBotCount > botsMaxCount) {
            throw TooManyBotsAddedException(botsMaxCount)
        }
        command.bots.forEach {
            saveBot.saveBot(it)
            thread(name = it.username.text) { playingGameLogic(it.username) }
        }
        log.info("Successfully added $addedBotCount bots with usernames: ${cmd.addedBotCmd.map { it.username }}")
    }

    override fun deleteBots(cmd: DeleteBotsUseCase.DeleteBotsCmd) {
        log.info("Removing ${cmd.deletedBotsUsernames.count()} bots with usernames: ${cmd.deletedBotsUsernames}")
        val command = cmd.toCommand()
        command.usernames.forEach { username ->
            val deletedBot = loadBot.loadBot(username)
            if (deletedBot != null) {
                wipeOutBotExistence(deletedBot)
            }
        }
        log.info("Successfully removed ${cmd.deletedBotsUsernames.count()} bots with usernames: ${cmd.deletedBotsUsernames}")
    }

    override fun deleteExpiredBots() {
        val expiredBots = loadBot.listBots().filter { it.expirationDate.isBefore(LocalDateTime.now()) }
        if (expiredBots.isNotEmpty()) {
            val expiredBotsCount = expiredBots.size
            val expiredBotsUsernames = expiredBots.map { it.username }
            log.info("Found $expiredBotsCount expired bots with usernames: $expiredBotsUsernames. Deleting listed bots")
            expiredBots.forEach { wipeOutBotExistence(it) }
            log.info("Successfully deleted $expiredBotsCount expired bots with usernames: $expiredBotsUsernames")
        }
    }

    private fun playingGameLogic(username: Username) {

        while (true) {
            try {
                var bot = loadBot.loadBot(username) ?: throw BotNotFound(username)
                val currentGameId = joinOrInitializeGame.joinOrInitialize(username)
                bot.currentGameId = currentGameId
                saveBot.saveBot(bot)

                Thread.sleep((Random.nextDouble() * 2000).toLong())
                while (bot.currentGameId != null) {
                    try {
                        bot = loadBot.loadBot(username) ?: throw BotNotFound(username)
                        Thread.sleep((Random.nextDouble() * 1000).toLong())
                        val boardAdditionalInfo = loadGameAdditionalInfo.loadGameAdditionalInfo(bot.currentGameId!!)
                            ?: throw BoardNotFoundException(bot.currentGameId!!)

                        if (isGameFinished(boardAdditionalInfo)) {
                            bot.currentGameId = null
                            saveBot.saveBot(bot)
                            continue
                        }

                        if (!isBlackPlayerPresent(boardAdditionalInfo) || !isMyTurn(bot, boardAdditionalInfo)) {
                            continue
                        }

                        val fenBoardPosition = loadGame.loadGame(bot.currentGameId!!)
                            ?: throw BoardNotFoundException(bot.currentGameId!!)

                        loadMovesAndPerformRandomMove(fenBoardPosition, bot)

                    } catch (boardNotFound: BoardNotFoundException) {
                        bot.currentGameId = null
                    } catch (topMovesCouldNotBeEstablished: TopMovesCouldNotBeEstablished) {
                        botResignGame.botResignGame(bot)
                        bot.currentGameId = null
                    } catch (ignored: InterruptedException) {
                    }
                }
            } catch (botNotFound: BotNotFound) {
                log.info("Bot with username: ${username.text} was removed. Stopping thread playing game")
                break
            }
        }
    }

    private fun loadMovesAndPerformRandomMove(
        fenBoardPosition: FenBoardPosition,
        bot: Bot
    ) {
        val loadedTopMoves = topMovesFacade.establishAndSaveIfUpdatedTopMoves(
            EstablishListOfTopMoves(
                bot.currentGameId!!,
                fenBoardPosition,
                bot.obtainedBestMovesCount,
                bot.searchedDepth
            )
        )

        if (loadedTopMoves.topMovesList.isEmpty()) {
            botResignGame.botResignGame(bot)
        } else {
            botPerformMove.performMove(
                PerformMove(bot, loadedTopMoves.topMovesList.random())
            )
        }
    }

    private fun isMyTurn(bot: Bot, boardAdditionalInfoView: BoardAdditionalInfo): Boolean {
        return boardAdditionalInfoView.currentPlayerColor == "WHITE"
                && bot.username.text == boardAdditionalInfoView.whitePlayerUsername.text
                ||
                boardAdditionalInfoView.currentPlayerColor == "BLACK"
                && bot.username.text == boardAdditionalInfoView.blackPlayerUsername!!.text
    }

    private fun isBlackPlayerPresent(boardAdditionalInfoView: BoardAdditionalInfo) =
        boardAdditionalInfoView.blackPlayerUsername != null

    private fun isGameFinished(boardAdditionalInfoView: BoardAdditionalInfo) =
        boardAdditionalInfoView.victoriousPlayerColor != null

    private fun wipeOutBotExistence(bot: Bot) {
        botResignGame.botResignGame(bot)
        botQuitNotYetStartedGame.quitNotYetStartedGame(bot)
        deleteBot.deleteBot(bot.username)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(BotService::class.java)
    }

}