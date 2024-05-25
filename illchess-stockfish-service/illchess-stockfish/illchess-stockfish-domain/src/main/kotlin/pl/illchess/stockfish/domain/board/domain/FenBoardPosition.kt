package pl.illchess.stockfish.domain.board.domain

data class FenBoardPosition(
    val value: String,
    val position: String = value.split(" ")[0],
    val color: String = value.split(" ")[1],
    val castlingPossible: String = value.split(" ")[2],
    val possibleEnPassantMove: String = value.split(" ")[3],
    val halfMovesCount: String = value.split(" ")[4],
    val movesCount: String = value.split(" ")[5],
)
