package pl.illchess.stockfish.adapter.evaluation.command.out.bash.util

import pl.illchess.stockfish.domain.board.domain.FenBoardPosition
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation
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

    fun startEngine(): Boolean {
        try {
            engineProcess = Runtime.getRuntime().exec(arrayOf("stockfish"))
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

    fun getBestMoveAndContinuation(fenPosition: FenBoardPosition): BestMoveAndContinuation {
        sendCommand("position fen ${fenPosition.value}")
        sendCommand("go depth 15")
        return getBestMoveAndContinuation()
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

    private fun getBestMoveAndContinuation(): BestMoveAndContinuation {
        val bestMoveAndContinuation = processReader!!.lines()
            .filter { it.contains("bestmove") || it.contains("info depth 15") }
            .limit(2)
            .toList()

        val bestMove: String = bestMoveAndContinuation.filter { it.contains("bestmove") }[0]
            .split(" ")[1]

        val continuation = bestMoveAndContinuation
            .filter { line: String -> line.contains("info depth 15") }[0]
            .split("pv")[2]
            .split(" ")
            .filter { it != "" }

        return BestMoveAndContinuation(bestMove, continuation)
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