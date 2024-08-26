package pl.illchess.player_info.adapter.user.command.out.jpa_streamer.entity

import io.quarkus.hibernate.orm.panache.PanacheEntityBase
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user")
open class UserEntity(
    @Id
    @Column(name = "id")
    open val id: UUID,

    @Column(name = "username")
    open val username: String,

    @Column(name = "current_ranking_points")
    open val currentRankingPoints: Int
) : PanacheEntityBase()