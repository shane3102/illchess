package pl.illchess.stockfish.application.bot.command

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.illchess.stockfish.application.bot.command.`in`.AddBotsUseCase
import pl.illchess.stockfish.application.bot.command.`in`.DeleteBotsUseCase
import pl.illchess.stockfish.application.bot.command.out.DeleteBot
import pl.illchess.stockfish.application.bot.command.out.LoadBot
import pl.illchess.stockfish.application.bot.command.out.SaveBot
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.board.command.out.BotPerformMove
import pl.illchess.stockfish.application.board.command.out.BotResignGame
import pl.illchess.stockfish.application.board.command.out.JoinOrInitializeBoard
import pl.illchess.stockfish.application.board.command.out.LoadBoard
import pl.illchess.stockfish.application.board.command.out.LoadBoardAdditionalInfo
import pl.illchess.stockfish.domain.board.domain.BoardAdditionalInfo
import pl.illchess.stockfish.domain.board.exception.BoardNotFoundException
import pl.illchess.stockfish.domain.bot.command.PerformMove
import pl.illchess.stockfish.domain.bot.domain.Bot
import pl.illchess.stockfish.domain.bot.domain.Username
import pl.illchess.stockfish.domain.bot.exception.BotNotFound
import pl.illchess.stockfish.domain.evaluation.exception.TopMovesCouldNotBeEstablished
import kotlin.concurrent.thread

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

    private var threadCache: MutableList<Thread> = mutableListOf()

    override fun addBots(cmd: AddBotsUseCase.AddBotsCmd) {
        log.info("Adding ${cmd.addedBotCmd.count()} bots")
        val command = cmd.toCommand()
        command.bots.forEach{
            saveBot.saveBot(it)
            threadCache.add(
                thread(name = it.username.text) { playingGameLogic(it.username) }
            )
        }
        log.info("Successfully added ${cmd.addedBotCmd.count()} bots")
    }

    override fun deleteBots(cmd: DeleteBotsUseCase.DeleteBotsCmd) {
        log.info("Removing ${cmd.deletedBotsUsernames.count()} bots")
        val command = cmd.toCommand()
        command.usernames.forEach{ username ->
            val interruptedThread = threadCache.first { it.name == username.text }
            interruptedThread.interrupt()
            deleteBot.deleteBot(username)
        }
        log.info("Successfully removed ${cmd.deletedBotsUsernames.count()} bots")
    }

    private fun playingGameLogic(username: Username) {
        val bot = loadBot.loadBot(username) ?: throw BotNotFound(username)

        while (true) {
            val currentBoardId = joinOrInitializeBoard.joinOrInitialize(username)
            bot.currentBoardId = currentBoardId
            saveBot.saveBot(bot)
            while (bot.currentBoardId != null) {
                try {
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
                } catch (ignored: InterruptedException) { }
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