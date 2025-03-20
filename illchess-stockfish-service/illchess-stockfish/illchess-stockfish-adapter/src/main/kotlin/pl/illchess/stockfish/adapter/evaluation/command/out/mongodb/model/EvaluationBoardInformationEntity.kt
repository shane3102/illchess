package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model

data class EvaluationBoardInformationEntity(
    val position: String,
    val color: String,
    val castlingPossible: String,
    val depth: Int? = null
)
