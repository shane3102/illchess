package pl.illchess.stockfish.application.bot.command

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.stockfish.application.board.command.out.*
import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase
import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase
import pl.illchess.stockfish.application.bot.command.out.DeleteBot
import pl.illchess.stockfish.application.bot.command.out.LoadBot
import pl.illchess.stockfish.application.bot.command.out.SaveBot
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.domain.board.domain.BoardAdditionalInfo
import pl.illchess.stockfish.domain.board.exception.BoardNotFoundException
import pl.illchess.stockfish.domain.bot.command.PerformMove
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username
import pl.illchess.stockfish.domain.bot.exception.BotNotFound
import pl.illchess.stockfish.domain.evaluation.exception.TopMovesCouldNotBeEstablished
import kotlin.concurrent.thread
import kotlin.random.Random

class BotService(
    private val saveBot: SaveBot,
    private val loadBot: LoadBot,
    private val deleteBot: DeleteBot,
    private val joinOrInitializeBoard: JoinOrInitializeBoard,
    private val loadBoard: LoadBoard,
    private val loadBoardAdditionalInfo: LoadBoardAdditionalInfo,
    private val loadTopMoves: LoadTopMoves,
    private val botPerformMove: BotPerformMove,
    private val botResignGame: BotResignGame
) : AddBotsUseCase, DeleteBotsUseCase {

    override fun addBots(cmd: AddBotsUseCase.AddBotsCmd) {
        log.info("Adding ${cmd.addedBotCmd.count()} bots with usernames: ${cmd.addedBotCmd.map { it.username }}")
        val command = cmd.toCommand()
        command.bots.forEach {
            saveBot.saveBot(it)
            thread(name = it.username.text) { playingGameLogic(it.username) }
        }
        log.info("Successfully added ${cmd.addedBotCmd.count()} bots with usernames: ${cmd.addedBotCmd.map { it.username }}")
    }

    override fun deleteBots(cmd: DeleteBotsUseCase.DeleteBotsCmd) {
        log.info("Removing ${cmd.deletedBotsUsernames.count()} bots with usernames: ${cmd.deletedBotsUsernames}")
        val command = cmd.toCommand()
        command.usernames.forEach { username ->
            val deletedBot = loadBot.loadBot(username)
            if (deletedBot != null) {
                botResignGame.botResignGame(deletedBot)
                deleteBot.deleteBot(username)
            }
        }
        log.info("Successfully removed ${cmd.deletedBotsUsernames.count()} bots with usernames: ${cmd.deletedBotsUsernames}")
    }

    private fun playingGameLogic(username: Username) {

        while (true) {
            try {
                val bot = loadBot.loadBot(username) ?: throw BotNotFound(username)
                val currentBoardId = joinOrInitializeBoard.joinOrInitialize(username)
                bot.currentBoardId = currentBoardId
                saveBot.saveBot(bot)

                Thread.sleep((Random.nextDouble() * 2000).toLong())
                while (bot.currentBoardId != null) {
                    try {
                        Thread.sleep((Random.nextDouble() * 1000).toLong())
                        val boardAdditionalInfo = loadBoardAdditionalInfo.loadBoardAdditionalInfo(bot.currentBoardId!!)
                            ?: throw BoardNotFoundException(bot.currentBoardId!!)

                        if (isGameFinished(boardAdditionalInfo)) {
                            bot.currentBoardId = null
                            saveBot.saveBot(bot)
                            continue
                        }

                        if (!isBlackPlayerPresent(boardAdditionalInfo) || !isMyTurn(bot, boardAdditionalInfo)) {
                            continue
                        }

                        val fenBoardPosition = loadBoard.loadBoard(bot.currentBoardId!!)
                            ?: throw BoardNotFoundException(bot.currentBoardId!!)

                        val loadedTopMoves = loadTopMoves.loadTopMoves(
                            fenBoardPosition,
                            bot.obtainedBestMovesCount,
                            bot.searchedDepth
                        ) ?: throw TopMovesCouldNotBeEstablished(bot.currentBoardId!!)

                        if (loadedTopMoves.topMovesList.isEmpty()) {
                            botResignGame.botResignGame(bot)
                        } else {
                            botPerformMove.performMove(
                                PerformMove(bot, loadedTopMoves.topMovesList.random())
                            )
                        }

                    } catch (boardNotFound: BoardNotFoundException) {
                        bot.currentBoardId = null
                    } catch (topMovesCouldNotBeEstablished: TopMovesCouldNotBeEstablished) {
                        botResignGame.botResignGame(bot)
                        bot.currentBoardId = null
                    } catch (ignored: InterruptedException) {
                    }
                }
            } catch (botNotFound: BotNotFound) {
                log.info("Bot with username: ${username.text} was removed. Stopping thread playing game")
                break
            }
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

    companion object {
        private val log: Logger = LoggerFactory.getLogger(BotService::class.java)
    }

}