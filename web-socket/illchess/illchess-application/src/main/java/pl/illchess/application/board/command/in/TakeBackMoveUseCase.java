package pl.illchess.application.board.command.in;

import java.util.UUID;

public interface TakeBackMoveUseCase {

    void takeBackMove(TakeBackMoveCmd cmd);

    record TakeBackMoveCmd(UUID boardId) {

    }
}
