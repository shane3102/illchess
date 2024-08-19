package pl.illchess.player_info.domain.game.model

data class PerformedMove(
    val startSquare: ChessSquare,
    val endSquare: ChessSquare,
    val stringValue: MoveAlgebraicNotation,
    val color: PieceColor
)