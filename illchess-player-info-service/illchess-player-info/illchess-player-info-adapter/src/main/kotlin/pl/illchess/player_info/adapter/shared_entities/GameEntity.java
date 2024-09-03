package pl.illchess.player_info.adapter.shared_entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@Table(name = "game")
@EqualsAndHashCode(callSuper = true)
public class GameEntity extends PanacheEntityBase {
    @Id
    @Column(name = "id")
    public UUID id;

    // white
    @PrimaryKeyJoinColumn(name = "white_user_id")
    @OneToOne(fetch = FetchType.EAGER)
    public UserEntity whiteUser;
    @Column(name = "white_ranking_points_before_game")
    public int whiteRankingPointsBeforeGame;
    @Column(name = "white_ranking_points_after_game")
    public int whiteRankingPointsAfterGame;
    @Column(name = "white_ranking_points_change")
    public int whiteRankingPointsChange;

    // black
    @PrimaryKeyJoinColumn(name = "black_user_id")
    @OneToOne(fetch = FetchType.EAGER)
    public UserEntity blackUser;
    @Column(name = "black_ranking_points_before_game")
    public int blackRankingPointsBeforeGame;
    @Column(name = "black_ranking_points_after_game")
    public int blackRankingPointsAfterGame;
    @Column(name = "black_ranking_points_change")
    public int blackRankingPointsChange;

    // rest
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
        this.winningPieceColor = winningPieceColor;
        this.performedMoves = performedMoves;
    }
}