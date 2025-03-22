package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

@MongoEntity(collection = "BOARD_BEST_MOVE_AND_CONTINUATION")
data class BestMoveAndContinuationEntity @BsonCreator constructor(
    @BsonId
    @BsonProperty("_id")
    val id: ObjectId,
    @BsonProperty("evaluationBoardInformation")
    val evaluationBoardInformation: EvaluationBoardInformationEntity,
    @BsonProperty("bestMove")
    val bestMove: String,
    @BsonProperty("continuation")
    val continuation: List<String>
)