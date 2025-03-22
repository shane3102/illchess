package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.BestMoveAndEvaluationMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.EvaluationBoardInformationMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository.BestMoveAndContinuationRepository
import pl.illchess.stockfish.application.evaluation.command.out.LoadBestMoveAndContinuation
import pl.illchess.stockfish.application.evaluation.command.out.SaveBestMoveAndContinuation
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.BestMoveAndContinuation

@ApplicationScoped
class BestMoveAndContinuationMongodbAdapter(
    private val repository: BestMoveAndContinuationRepository
) : LoadBestMoveAndContinuation, SaveBestMoveAndContinuation {

    override fun loadBestMoveAndContinuation(evaluationBoardInformation: EvaluationBoardInformation): BestMoveAndContinuation? {
        val evaluationBoardInformationEntity = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        val loadedEntity = repository.findByEvaluationBoardInformation(evaluationBoardInformationEntity)
        return if (loadedEntity == null) null else BestMoveAndEvaluationMapper.toDomain(loadedEntity)
    }

    override fun saveBestMoveAndContinuation(
        evaluationBoardInformation: EvaluationBoardInformation,
        bestMoveAndContinuation: BestMoveAndContinuation
    ) {
        val evaluationBoardInformationEntity = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        val loadedEntity = repository.findByEvaluationBoardInformation(evaluationBoardInformationEntity)
        val id = loadedEntity?.id
        val entity = BestMoveAndEvaluationMapper.toEntity(evaluationBoardInformation, bestMoveAndContinuation, id)
        repository.persistOrUpdate(entity)
    }

}