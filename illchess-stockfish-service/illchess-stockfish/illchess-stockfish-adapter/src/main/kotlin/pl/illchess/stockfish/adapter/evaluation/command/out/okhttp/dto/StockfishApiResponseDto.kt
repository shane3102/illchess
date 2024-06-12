package pl.illchess.stockfish.adapter.evaluation.command.out.okhttp.dto

data class StockfishApiResponseDto(
    val success: Boolean,
    val evaluation: String,
    val mate: String?,
    val bestmove: String,
    val continuation: String,
)