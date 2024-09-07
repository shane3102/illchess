package pl.illchess.player_info.adapter.shared_entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Data
@Entity
@NoArgsConstructor
@Table(name = "game")
@EqualsAndHashCode(callSuper = true)
public class GameEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    public UUID id;

    // white
    @JoinColumn(name = "white_user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.MERGE)
    public UserEntity whiteUser;
    @Column(name = "white_ranking_points_before_game")
    public int whiteRankingPointsBeforeGame;
    @Column(name = "white_ranking_points_after_game")
    public int whiteRankingPointsAfterGame;
    @Column(name = "white_ranking_points_change")
    public int whiteRankingPointsChange;

    // black
    @JoinColumn(name = "black_user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.MERGE)
    public UserEntity blackUser;
    @Column(name = "black_ranking_points_before_game")
    public int blackRankingPointsBeforeGame;
    @Column(name = "black_ranking_points_after_game")
    public int blackRankingPointsAfterGame;
    @Column(name = "black_ranking_points_change")
    public int blackRankingPointsChange;

    // rest
    @Column(name = "end_time")
    public LocalDateTime endTime;
    @Column(name = "winning_piece_color")
    public String winningPieceColor;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "performed_moves")
    public List<PerformedMoveEntity> performedMoves;

    public GameEntity(
            UUID id,
            UserEntity whiteUser,
            int whiteRankingPointsBeforeGame,
            int whiteRankingPointsAfterGame,
            int whiteRankingPointsChange,
            UserEntity blackUser,
            int blackRankingPointsBeforeGame,
            int blackRankingPointsAfterGame,
            int blackRankingPointsChange,
            LocalDateTime endTime,
            String winningPieceColor,
            List<PerformedMoveEntity> performedMoves
    ) {
        this.id = id;
        this.whiteUser = whiteUser;
        this.whiteRankingPointsBeforeGame = whiteRankingPointsBeforeGame;
        this.whiteRankingPointsAfterGame = whiteRankingPointsAfterGame;
        this.whiteRankingPointsChange = whiteRankingPointsChange;
        this.blackUser = blackUser;
        this.blackRankingPointsBeforeGame = blackRankingPointsBeforeGame;
        this.blackRankingPointsAfterGame = blackRankingPointsAfterGame;
        this.blackRankingPointsChange = blackRankingPointsChange;
        this.endTime = endTime;
        this.winningPieceColor = winningPieceColor;
        this.performedMoves = performedMoves;
    }
}
