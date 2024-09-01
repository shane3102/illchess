package pl.illchess.player_info.adapter.shared_entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id")
    public UUID id;

    @Column(name = "username")
    public String username;

    @Column(name = "current_ranking_points")
    public int currentRankingPoints;

    public UserEntity(UUID id, String username, int currentRankingPoints) {
        this.id = id;
        this.username = username;
        this.currentRankingPoints = currentRankingPoints;
    }
}
