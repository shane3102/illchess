package pl.illchess.stockfish.domain.board.domain

data class FenBoardPosition(
    val fullString: String,
    val position: String = fullString.split(" ")[0],
    val color: String = fullString.split(" ")[1],
    val castlingPossible: String = fullString.split(" ")[2],
    val possibleEnPassantMove: String = fullString.split(" ")[3],
    val halfMovesCount: String = fullString.split(" ")[4],
    val movesCount: String = fullString.split(" ")[5],
)
