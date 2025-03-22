package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb

import io.quarkus.arc.properties.IfBuildProperty
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.EvaluationBoardInformationMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.mapper.TopMovesMapper
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository.TopMovesRepository
import pl.illchess.stockfish.application.evaluation.command.out.LoadTopMoves
import pl.illchess.stockfish.application.evaluation.command.out.SaveTopMoves
import pl.illchess.stockfish.domain.board.domain.EvaluationBoardInformation
import pl.illchess.stockfish.domain.evaluation.domain.TopMoves

@ApplicationScoped
@IfBuildProperty(name = "calculations.caching.enabled", stringValue = "true")
class TopMovesMongodbAdapter(
    private val repository: TopMovesRepository
) : LoadTopMoves, SaveTopMoves {

    override fun loadTopMoves(evaluationBoardInformation: EvaluationBoardInformation): TopMoves? {
        val id = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        val loadedEntity = repository.findByEvaluationBoardInformation(id)
        return if (loadedEntity == null) null else TopMovesMapper.toDomain(loadedEntity)
    }

    override fun saveTopMoves(evaluationBoardInformation: EvaluationBoardInformation, topMoves: TopMoves) {
        val evaluationBoardInformationEntity = EvaluationBoardInformationMapper.toEntity(evaluationBoardInformation)
        val loadedEntity = repository.findByEvaluationBoardInformation(evaluationBoardInformationEntity)
        val id = loadedEntity?.id
        val entity = TopMovesMapper.toEntity(evaluationBoardInformation, topMoves, id)
        repository.persistOrUpdate(entity)
    }
}