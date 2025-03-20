package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper

import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.TopMovesEntity
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

class TopMovesMapper {
    companion object {
        fun toEntity(evaluationBoardInformation: EvaluationBoardInformation, topMoves: TopMoves) = TopMovesEntity(
            EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation),
            topMoves.topMovesList
        )

        fun toDomain(topMovesEntity: TopMovesEntity) = TopMoves(
            topMovesEntity.topMoves
        )
    }
}