package pl.illchess.player_info.adapter.shared_entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@RegisterForReflection
public class PerformedMoveEntity {
    public String startSquare;
    public String endSquare;
    public String stringValue;
    public String color;

    public PerformedMoveEntity(String startSquare, String endSquare, String stringValue, String color) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.stringValue = stringValue;
        this.color = color;
    }
}
