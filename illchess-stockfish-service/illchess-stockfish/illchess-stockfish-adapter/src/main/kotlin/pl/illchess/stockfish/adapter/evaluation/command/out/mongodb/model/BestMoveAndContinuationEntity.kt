package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.codecs.pojo.annotations.BsonId

@MongoEntity(collection = "BOARD_BEST_MOVE_AND_CONTINUATION")
data class BestMoveAndContinuationEntity(
    @BsonId
    val evaluationBoardInformation: EvaluationBoardInformationEntity,
    val bestMove: String,
    val continuation: List<String>
)