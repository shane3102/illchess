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
) : LoadBoardEvaluation, SaveBoardEvaluation {

    override fun loadBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation): Evaluation? {
        val evaluationBoardInformationEntity = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        val loadedEntity = repository.findByEvaluationBoardInformation(evaluationBoardInformationEntity)
        return if (loadedEntity == null) null else EvaluationMapper.toDomain(loadedEntity)
    }

    override fun saveBoardEvaluation(evaluationBoardInformation: EvaluationBoardInformation, evaluation: Evaluation) {
        val evaluationBoardInformationEntity = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        val loadedEntity = repository.findByEvaluationBoardInformation(evaluationBoardInformationEntity)
        val id = loadedEntity?.id
        val entity = EvaluationMapper.toEntity(evaluationBoardInformation, evaluation, id)
        repository.persistOrUpdate(entity)
    }
}