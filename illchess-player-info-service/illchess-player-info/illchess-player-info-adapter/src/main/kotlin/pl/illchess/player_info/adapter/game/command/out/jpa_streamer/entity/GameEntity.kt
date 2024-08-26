package pl.illchess.player_info.adapter.game.command.out.jpa_streamer.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.PrimaryKeyJoinColumn
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import pl.illchess.player_info.adapter.user.command.out.jpa_streamer.entity.UserEntity
import java.util.UUID

@Entity
@Table(name = "game")
open class GameEntity(
    @Id
    @Column(name = "id")
    open val id: UUID,

    // white
    @PrimaryKeyJoinColumn(name = "white_user_id")
    @OneToOne(fetch = FetchType.EAGER)
    open val whiteUser: UserEntity,
    @Column(name = "white_ranking_points_before_game")
    open val whiteRankingPointsBeforeGame: Int,
    @Column(name = "white_ranking_points_after_game")
    open val whiteRankingPointsAfterGame: Int,
    @Column(name = "white_ranking_points_change")
    open val whiteRankingPointsChange: Int,

    // black
    @PrimaryKeyJoinColumn(name = "black_user_id")
    @OneToOne(fetch = FetchType.EAGER)
    open val blackUser: UserEntity,
    @Column(name = "black_ranking_points_before_game")
    open val blackRankingPointsBeforeGame: Int,
    @Column(name = "black_ranking_points_after_game")
    open val blackRankingPointsAfterGame: Int,
    @Column(name = "black_ranking_points_change")
    open val blackRankingPointsChange: Int,

    // rest
    @Column(name = "winning_piece_color")
    open val winningPieceColor: String,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "performed_moves")
    open val performedMoves: List<PerformedMoveEntity>
) : PanacheEntityBase() {

    class PerformedMoveEntity(
        val startSquare: String,
        val endSquare: String,
        val stringValue: String,
        val color: String
    )

}