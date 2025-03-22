package pl.illchess.stockfish.adapter.evaluation.command.out.bash.util

import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves
import pl.illchess.stockfish.domain.evaluation.exception.EvaluationByEngineCouldNotBeEstablished
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import java.util.stream.Stream

class StockfishConnector {
    private var engineProcess: Process? = null
    private var processReader: BufferedReader? = null
    private var processWriter: OutputStreamWriter? = null

    fun startEngine(path: String): Boolean {
        try {
            engineProcess = Runtime.getRuntime().exec(arrayOf(path))
            processReader = BufferedReader(InputStreamReader(engineProcess!!.inputStream))
            processWriter = OutputStreamWriter(engineProcess!!.outputStream)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    private fun sendCommand(command: String) {
        try {
            processWriter!!.write(command + "\n")
            processWriter!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getBestMoveAndContinuation(fenPosition: FenBoardPosition, depth: Int): BestMoveAndContinuation {
        sendCommand("position fen ${fenPosition.value}")
        sendCommand("go depth $depth")
        return getBestMoveAndContinuation(depth)
    }

    fun getTopMovesByNumber(
        fenPosition: FenBoardPosition,
        moveCount: Int,
        depth: Int
    ): TopMoves {
        sendCommand("position fen ${fenPosition.value}")
        sendCommand("setoption name multipv value $moveCount")
        sendCommand("go depth $depth")
        return getListOfTopMoves(moveCount)
    }

    fun getEvaluation(fenPosition: FenBoardPosition): Evaluation {
        sendCommand("position fen ${fenPosition.value}")
        sendCommand("eval")
        return getEvaluation()
    }

    private fun getEvaluation(): Evaluation {
        return processReader!!.lines()
            .filter { line: String -> line.contains("Final evaluation") }
            .map { line: String ->
                line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            }
            .map { line: Array<String>? ->
                val lineSplitByWhiteSpace = Arrays.stream(line)
                lineSplitByWhiteSpace
                    .filter { l: String -> l.contains("-") || l.contains("+") || l == "none" }
            }
            .findFirst()
            .flatMap { obj: Stream<String> -> obj.findFirst() }
            .map { obj: String -> if (obj == "none") Evaluation(0.0) else Evaluation(obj.toDouble()) }
            .orElseThrow { EvaluationByEngineCouldNotBeEstablished() }
    }

    private fun getBestMoveAndContinuation(depth: Int):BestMoveAndContinuation {
        val bestMoveAndContinuation = processReader!!.lines()
            .filter { it.contains("bestmove") || it.contains("info depth $depth") }
            .limit(2)
            .toList()

        val bestMove: String = bestMoveAndContinuation.filter { it.contains("bestmove") }[0]
            .split(" ")[1]

        val continuation = bestMoveAndContinuation
            .filter { line: String -> line.contains("info depth $depth") }[0]
            .split("pv")[2]
            .split(" ")
            .filter { it != "" }

        return BestMoveAndContinuation(bestMove, continuation)
    }

    private fun getListOfTopMoves(moveNumber: Int): TopMoves {
        val allTopMoves: List<String> = processReader!!.lines()
            .takeWhile { !it.contains("bestmove") }
            .filter { it.contains("info depth") && it.contains("seldepth") }
            .toList()

        val topMovesByHighestDepth = allTopMoves.sortedBy { it.split(" ")[3] }.stream()
            .limit(moveNumber.toLong())
            .toList()

        val topMovesList = topMovesByHighestDepth
            .map { it.split("pv")[2].split(" ")[1] }

        return TopMoves(topMovesList)
    }

    fun stopEngine() {
        try {
            sendCommand("quit")
            processReader!!.close()
            processWriter!!.close()
        } catch (e: IOException) {
        }
    }
}