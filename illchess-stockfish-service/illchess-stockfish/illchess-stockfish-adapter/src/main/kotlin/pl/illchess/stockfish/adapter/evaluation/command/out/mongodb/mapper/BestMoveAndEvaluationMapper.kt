package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper

import org.bson.types.ObjectId
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.BestMoveAndContinuationEntity
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

class BestMoveAndEvaluationMapper {
    companion object {
        fun toEntity(
            evaluationBoardInformation: EvaluationBoardInformation,
            bestMoveAndContinuation: BestMoveAndContinuation,
            id: ObjectId? = null
        ) = BestMoveAndContinuationEntity(
            id ?: ObjectId.get(),
            EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation),
            bestMoveAndContinuation.bestMove,
            bestMoveAndContinuation.continuation
        )

        fun toDomain(
            bestMoveAndContinuationEntity: BestMoveAndContinuationEntity
        ) = BestMoveAndContinuation(
            bestMoveAndContinuationEntity.bestMove,
            bestMoveAndContinuationEntity.continuation
        )
    }
}