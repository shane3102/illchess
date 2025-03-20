package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.EvaluationBoardInformationMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.TopMovesMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationBoardInformationEntity
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository.TopMovesRepository
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

@ApplicationScoped
class TopMovesMongodbAdapter(
    private val repository: TopMovesRepository
) : LoadTopMoves, SaveTopMoves {

    override fun loadTopMoves(evaluationBoardInformation: EvaluationBoardInformation): TopMoves? {
        val id = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        return TopMovesMapper.toDomain(repository.findById(id))
    }

    override fun saveTopMoves(evaluationBoardInformation: EvaluationBoardInformation, topMoves: TopMoves) {
        val entity = TopMovesMapper.toEntity(evaluationBoardInformation, topMoves)
        repository.persistOrUpdate(entity)
    }
}