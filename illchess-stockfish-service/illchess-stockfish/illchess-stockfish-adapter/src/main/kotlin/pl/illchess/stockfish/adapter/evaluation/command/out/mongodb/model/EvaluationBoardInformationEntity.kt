package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

data class EvaluationBoardInformationEntity @BsonCreator constructor(
    @BsonProperty("position")
    val position: String,
    @BsonProperty("color")
    val color: String,
    @BsonProperty("castlingPossible")
    val castlingPossible: String,
    @BsonProperty("depth")
    val depth: Int? = null
)
