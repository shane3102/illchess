package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationBoardInformationEntity
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.TopMovesEntity

@ApplicationScoped
class TopMovesRepository: PanacheMongoRepositoryBase<TopMovesEntity, EvaluationBoardInformationEntity>