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
        val id = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        return BestMoveAndEvaluationMapper.toDomain(repository.findById(id))
    }

    override fun saveBestMoveAndContinuation(
        evaluationBoardInformation: EvaluationBoardInformation,
        bestMoveAndContinuation: BestMoveAndContinuation
    ) {
        val entity = BestMoveAndEvaluationMapper.toEntity(evaluationBoardInformation, bestMoveAndContinuation)
        repository.persistOrUpdate(entity)
    }

}