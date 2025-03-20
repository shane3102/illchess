package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model

import io.quarkus.mongodb.panache.common.MongoEntity
import org.bson.codecs.pojo.annotations.BsonId

@MongoEntity(collection = "BOARD_TOP_MOVES")
data class TopMovesEntity(
    @BsonId
    val evaluationBoardInformationEntity: EvaluationBoardInformationEntity,
    val topMoves: List<String>
)
