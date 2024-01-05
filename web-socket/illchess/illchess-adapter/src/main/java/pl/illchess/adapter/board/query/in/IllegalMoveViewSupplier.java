package pl.illchess.adapter.board.query.in;

import org.springframework.context.event.EventListener;
import pl.illchess.application.board.query.out.model.IllegalMoveView;
import pl.illchess.domain.board.event.MoveIllegal;

public interface IllegalMoveViewSupplier {

    @EventListener(MoveIllegal.class)
    IllegalMoveView sendIllegalMoveViewInfo(MoveIllegal event);
}
