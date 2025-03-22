package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper

import org.bson.types.ObjectId
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationEntity
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

class EvaluationMapper {
    companion object {
        fun toEntity(evaluationBoardInformation: EvaluationBoardInformation,evaluation: Evaluation, id: ObjectId? = null) = EvaluationEntity(
            id?: ObjectId.get(),
            EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation),
            evaluation.value
        )

        fun toDomain(evaluationEntity: EvaluationEntity) = Evaluation(
            evaluationEntity.evaluation
        )
    }
}