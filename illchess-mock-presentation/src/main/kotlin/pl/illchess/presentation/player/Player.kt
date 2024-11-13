package pl.illchess.presentation.player

import java.util.UUID
import kotlin.random.Random
import pl.illchess.presentation.exception.BoardNotFound
import pl.illchess.presentation.okhttp.IllChessGameAdapter
import pl.illchess.presentation.okhttp.IllChessGameAdapter.BoardAdditionalInfoView
import pl.illchess.presentation.okhttp.IllChessGameAdapter.MovePieceRequest
import pl.illchess.presentation.okhttp.IllChessStockFishAdapter

class Player(
    private val username: String,
    private var currentBoardId: UUID? = null,
    private val moveCount: Int = 10,
    private val stockfishAdapter: IllChessStockFishAdapter = IllChessStockFishAdapter(),
    private val gameAdapter: IllChessGameAdapter = IllChessGameAdapter()
) {

    fun play() {
        while (true) {
            Thread.sleep((Random.nextDouble() * 5000).toLong())
            val initializedBoardResponse = gameAdapter.joinOrInitialize(
                IllChessGameAdapter.InitializeNewBoardRequest(username)
            )

            currentBoardId = initializedBoardResponse?.id

            while (currentBoardId != null) {
                Thread.sleep((Random.nextDouble() * 1000).toLong())
                try {
                    if (currentBoardId == null) continue
                    val boardAdditionalInfoView = gameAdapter.loadBoardAdditionalInfoView(currentBoardId!!)
                    if (isGameFinished(boardAdditionalInfoView!!)) {
                        currentBoardId = null
                        continue
                    }
                    if (!isBlackPlayerPresent(boardAdditionalInfoView)) {
                        continue
                    }
                    if (!isMyTurn(boardAdditionalInfoView)) {
                        continue
                    }
                    val moveRequest = toMovePieceRequest(
                        stockfishAdapter.loadTopMoves(currentBoardId!!, moveCount)!!.topMoves.random()
                    )

                    gameAdapter.performMove(moveRequest)
                } catch (e: BoardNotFound) {
                    currentBoardId = null
                }

            }
        }
    }

    private fun isMyTurn(boardAdditionalInfoView: BoardAdditionalInfoView): Boolean {
        return boardAdditionalInfoView.currentPlayerColor == "WHITE" && username == boardAdditionalInfoView.whitePlayer.username ||
                boardAdditionalInfoView.currentPlayerColor == "BLACK" && username == boardAdditionalInfoView.blackPlayer!!.username
    }

    private fun isBlackPlayerPresent(boardAdditionalInfoView: BoardAdditionalInfoView) =
        boardAdditionalInfoView.blackPlayer != null

    private fun isGameFinished(boardAdditionalInfoView: BoardAdditionalInfoView) =
        boardAdditionalInfoView.victoriousPlayerColor != null

    private fun toMovePieceRequest(topMoveStockfish: String): MovePieceRequest {
        val startSquare = topMoveStockfish.substring(0, 2).uppercase()
        val endSquare = topMoveStockfish.substring(2, 4).uppercase()
        val promotePieceType = if (topMoveStockfish.length == 5) when (topMoveStockfish.substring(4, 4)) {
            "q" -> "QUEEN"
            "r" -> "ROOK"
            "b" -> "BISHOP"
            "n", "k" -> "KNIGHT"
            else -> "QUEEN"
        } else null

        return MovePieceRequest(currentBoardId!!, startSquare, endSquare, promotePieceType, username)
    }

}