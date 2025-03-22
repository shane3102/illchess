package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper

import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationBoardInformationEntity
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation

class EvaluationBoardInformationMapper {
    companion object {
        fun toEntity(evaluationBoardInformation: EvaluationBoardInformation) = EvaluationBoardInformationEntity(
            evaluationBoardInformation.position,
            evaluationBoardInformation.color,
            evaluationBoardInformation.castlingPossible,
            evaluationBoardInformation.depth
        )
    }
}