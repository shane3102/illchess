package pl.illchess.stockfish.domain.board.domain

data class FenBoardPosition(
    val value: String,
    val position: String = getFenElementByPositionOrDefaultTo(value, 0, ""),
    val color: String = getFenElementByPositionOrDefaultTo(value, 1, "w"),
    val castlingPossible: String = getFenElementByPositionOrDefaultTo(value, 2, "KQkq"),
    val possibleEnPassantMove: String = getFenElementByPositionOrDefaultTo(value, 3, "-"),
    val halfMovesCount: String = getFenElementByPositionOrDefaultTo(value, 4, "0"),
    val movesCount: String = getFenElementByPositionOrDefaultTo(value, 5, "0"),
) {
    companion object {
        private fun getFenElementByPositionOrDefaultTo(
            value: String,
            position: Int,
            default: String
        ): String {
            val split = value.split(" ")
            return if (split.size <= position) default else split[position]
        }
    }
}
