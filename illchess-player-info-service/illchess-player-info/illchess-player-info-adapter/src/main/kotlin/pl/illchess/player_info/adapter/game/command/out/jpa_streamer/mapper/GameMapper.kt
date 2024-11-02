package pl.illchess.player_info.adapter.game.command.out.jpa_streamer.mapper

import pl.illchess.player_info.adapter.shared_entities.GameEntity
import pl.illchess.player_info.adapter.shared_entities.PerformedMoveEntity
import pl.illchess.player_info.adapter.player.command.out.jpa_streamer.mapper.PlayerMapper
import pl.illchess.player_info.domain.game.model.ChessSquare
import pl.illchess.player_info.domain.game.model.EndTime
import pl.illchess.player_info.domain.game.model.Game
import pl.illchess.player_info.domain.game.model.GameId
import pl.illchess.player_info.domain.game.model.GameResult
import pl.illchess.player_info.domain.game.model.MoveAlgebraicNotation
import pl.illchess.player_info.domain.game.model.PerformedMove
import pl.illchess.player_info.domain.game.model.PieceColor
import pl.illchess.player_info.domain.game.model.PlayerGameInfo
import pl.illchess.player_info.domain.player.model.PlayerRankingPoints

class GameMapper {
    companion object {
        fun toEntity(game: Game) = GameEntity(
            game.id.uuid,
            PlayerMapper.toEntity(game.whitePlayerGameInfo.player),
            game.whitePlayerGameInfo.rankingPointsBeforeGame.value,
            game.whitePlayerGameInfo.rankingPointsAfterGame.value,
            game.whitePlayerGameInfo.rankingPointsChange.value,
            PlayerMapper.toEntity(game.blackPlayerGameInfo.player),
            game.blackPlayerGameInfo.rankingPointsBeforeGame.value,
            game.blackPlayerGameInfo.rankingPointsAfterGame.value,
            game.blackPlayerGameInfo.rankingPointsChange.value,
            game.endTime.time,
            game.gameResult.name,
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
            PlayerGameInfo(
                PlayerMapper.toModel(gameEntity.whiteUser),
                PlayerRankingPoints(gameEntity.whiteRankingPointsBeforeGame),
                PlayerRankingPoints(gameEntity.whiteRankingPointsAfterGame),
                PlayerRankingPoints(gameEntity.whiteRankingPointsChange)
            ),
            PlayerGameInfo(
                PlayerMapper.toModel(gameEntity.blackUser),
                PlayerRankingPoints(gameEntity.blackRankingPointsBeforeGame),
                PlayerRankingPoints(gameEntity.blackRankingPointsAfterGame),
                PlayerRankingPoints(gameEntity.blackRankingPointsChange)
            ),
            GameResult.of(gameEntity.gameResult),
            EndTime(gameEntity.endTime),
            gameEntity.performedMoves.map {
                PerformedMove(
                    ChessSquare.of(it.startSquare),
                    ChessSquare.of(it.endSquare),
                    MoveAlgebraicNotation(it.stringValue),
                    PieceColor.of(it.color)
                )
            }
        )
    }
}