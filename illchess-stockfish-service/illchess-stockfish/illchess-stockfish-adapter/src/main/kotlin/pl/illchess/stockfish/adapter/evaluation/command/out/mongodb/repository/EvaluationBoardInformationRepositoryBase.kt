package pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.repository

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase
import io.quarkus.mongodb.panache.PanacheQuery
import pl.illchess.stockfish.adapter.evaluation.command.out.mongodb.model.EvaluationBoardInformationEntity

interface EvaluationBoardInformationRepositoryBase<T>: PanacheMongoRepositoryBase<T, EvaluationBoardInformationEntity> {

    fun findByEvaluationBoardInformation(evaluationBoardInformationEntity: EvaluationBoardInformationEntity): T? {
        val params: HashMap<String, Any> = hashMapOf(
            "evaluationBoardInformation.position" to evaluationBoardInformationEntity.position,
            "evaluationBoardInformation.color" to evaluationBoardInformationEntity.color,
            "evaluationBoardInformation.castlingPossible" to evaluationBoardInformationEntity.castlingPossible
        )

        val query: PanacheQuery<T>
        if (evaluationBoardInformationEntity.depth != null) {
            params["evaluationBoardInformation.depth"] = evaluationBoardInformationEntity.depth
            query = find(
                """
                    {
                        'evaluationBoardInformation.position': :evaluationBoardInformation.position,
                        'evaluationBoardInformation.color': :evaluationBoardInformation.color,
                        'evaluationBoardInformation.castlingPossible': :evaluationBoardInformation.castlingPossible,
                        'evaluationBoardInformation.depth': :evaluationBoardInformation.depth,
                    }
                """.trimIndent(),
                params
            )
        } else {
            query = find(
                """
                    {
                        'evaluationBoardInformation.position': :evaluationBoardInformation.position,
                        'evaluationBoardInformation.color': :evaluationBoardInformation.color,
                        'evaluationBoardInformation.castlingPossible': :evaluationBoardInformation.castlingPossible,
                    }
                """.trimIndent(),
                params
            )
        }

        return query
            .firstResultOptional<T>()
            .orElse(null)
    }

}