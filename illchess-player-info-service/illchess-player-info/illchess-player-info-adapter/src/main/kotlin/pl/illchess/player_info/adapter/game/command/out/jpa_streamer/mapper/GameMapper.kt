package pl.illchess.player_info.adapter.game.command.out.jpa_streamer.mapper

import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.adapter.shared_entities.PerformedMoveEntity
import pl.illchess.player_info.adapter.user.command.out.jpa_streamer.mapper.UserMapper
import pl.illchess.player_info.domain.game.model.ChessSquare
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.MoveAlgebraicNotation
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.game.model.UserGameInfo
import pl.illchess.player_info.domain.user.model.UserRankingPoints

class GameMapper {
    companion object {
        fun toEntity(game: Game) = GameEntity(
            game.id.uuid,
            UserMapper.toEntity(game.whiteUserGameInfo.user),
            game.whiteUserGameInfo.rankingPointsBeforeGame.value,
            game.whiteUserGameInfo.rankingPointsAfterGame.value,
            game.whiteUserGameInfo.rankingPointsChange.value,
            UserMapper.toEntity(game.blackUserGameInfo.user),
            game.blackUserGameInfo.rankingPointsBeforeGame.value,
            game.blackUserGameInfo.rankingPointsAfterGame.value,
            game.blackUserGameInfo.rankingPointsChange.value,
            game.winningPieceColor.name,
            game.performedMoves.map {
                PerformedMoveEntity(
                    it.startSquare.name,
                    it.endSquare.name,
                    it.stringValue.value,
                    it.color.name
                )
            }.toList()
        )

        fun toModel(gameEntity: GameEntity) = Game(
            GameId(gameEntity.id),
            UserGameInfo(
                UserMapper.toModel(gameEntity.whiteUser),
                UserRankingPoints(gameEntity.whiteRankingPointsBeforeGame),
                UserRankingPoints(gameEntity.whiteRankingPointsAfterGame),
                UserRankingPoints(gameEntity.whiteRankingPointsChange)
            ),
            UserGameInfo(
                UserMapper.toModel(gameEntity.blackUser),
                UserRankingPoints(gameEntity.blackRankingPointsBeforeGame),
                UserRankingPoints(gameEntity.blackRankingPointsAfterGame),
                UserRankingPoints(gameEntity.blackRankingPointsChange)
            ),
            PieceColor.of(gameEntity.winningPieceColor),
            gameEntity.performedMoves.map { PerformedMove(
                ChessSquare.of(it.startSquare),
                ChessSquare.of(it.endSquare),
                MoveAlgebraicNotation(it.stringValue),
                PieceColor.of(it.color)
            ) }
        )
    }
}