package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper

import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationEntity
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

class EvaluationMapper {
    companion object {
        fun toEntity(evaluationBoardInformation: EvaluationBoardInformation,evaluation: Evaluation) = EvaluationEntity(
            EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation),
            evaluation.value
        )

        fun toDomain(evaluationEntity: EvaluationEntity) = Evaluation(
            evaluationEntity.evaluation
        )
    }
}