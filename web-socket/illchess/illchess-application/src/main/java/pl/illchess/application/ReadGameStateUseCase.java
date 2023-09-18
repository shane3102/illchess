package pl.illchess.application;

import java.util.UUID;

public interface ReadGameStateUseCase {

    String readChessBoard(UUID chessBoardId);
}
