package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository

import jakarta.enterprise.context.ApplicationScoped
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationEntity

@ApplicationScoped
class EvaluationRepository : EvaluationBoardInformationRepositoryBase<EvaluationEntity>