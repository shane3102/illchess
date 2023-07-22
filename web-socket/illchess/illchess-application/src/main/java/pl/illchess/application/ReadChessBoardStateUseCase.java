package pl.illchess.application;

import java.util.UUID;

public interface ReadChessBoardStateUseCase {

    String readChessBoard(UUID chessBoardId);
}
