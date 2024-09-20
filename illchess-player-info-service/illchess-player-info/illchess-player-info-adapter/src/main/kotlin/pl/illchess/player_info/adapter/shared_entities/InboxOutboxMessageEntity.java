package pl.illchess.player_info.adapter.shared_entities;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@NoArgsConstructor
@Table(name = "inbox_outbox_message")
@EqualsAndHashCode(callSuper = true)
public class InboxOutboxMessageEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    public UUID id;

    @Column(name = "retry_count")
    public int retryCount;

    @Column(name = "occurred_on")
    public OffsetDateTime occurredOn;

    @Column(name = "class_name")
    public String className;

    @Column(name = "content")
    @JdbcTypeCode(SqlTypes.JSON)
    public JsonNode node;

    public InboxOutboxMessageEntity(UUID id, int retryCount, OffsetDateTime occurredOn, String className, JsonNode node) {
        this.id = id;
        this.retryCount = retryCount;
        this.occurredOn = occurredOn;
        this.className = className;
        this.node = node;
    }

    public JsonNode getNode() {
        return node;
    }
}
