package pl.illchess.stockfish.domain.board.domain

data class EvaluationBoardInformation(
    val position: String,
    val color: String,
    val castlingPossible: String,
    val depth: Int? = null
) {
    constructor(fenBoardPosition: FenBoardPosition, depth: Int? = null): this(
        fenBoardPosition.position,
        fenBoardPosition.color,
        fenBoardPosition.castlingPossible,
        depth
    )
}