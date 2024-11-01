package pl.illchess.player_info.adapter.shared_entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@NoArgsConstructor
@Table(name = "player")
@EqualsAndHashCode(callSuper = true)
public class PlayerEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    public UUID id;

    @Column(name = "username")
    public String username;

    @Column(name = "current_ranking_points")
    public int currentRankingPoints;

    public PlayerEntity(UUID id, String username, int currentRankingPoints) {
        this.id = id;
        this.username = username;
        this.currentRankingPoints = currentRankingPoints;
    }
}
