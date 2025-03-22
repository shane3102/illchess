package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.TopMovesEntity

@ApplicationScoped
class TopMovesRepository : EvaluationBoardInformationRepositoryBase<TopMovesEntity>