package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.EvaluationBoardInformationMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.EvaluationMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository.EvaluationRepository
import pl.illchess.stockfish.application.evaluation.command.out.LoadBoardEvaluation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBoardEvaluation
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.Evaluation

@ApplicationScoped
class EvaluationMongodbAdapter(
    private val repository: EvaluationRepository
): LoadBoardEvaluation, SaveBoardEvaluation {

    override fun loadBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation): Evaluation? {
        val id = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        return EvaluationMapper.toDomain(repository.findById(id))
    }

    override fun saveBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation, evaluation: Evaluation) {
        val entity = EvaluationMapper.toEntity(evaluationBoardInformation, evaluation)
        repository.persistOrUpdate(entity)
    }
}