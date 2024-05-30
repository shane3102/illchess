package pl.illchess.stockfish.adapter.board.command.`in`.rest.dto

data class BestMoveAndContinuationResponse(val bestMove: String, val continuation: List<String>)
