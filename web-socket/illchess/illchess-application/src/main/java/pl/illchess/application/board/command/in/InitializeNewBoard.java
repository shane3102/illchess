package pl.illchess.application.board.command.in;

import pl.illchess.domain.board.model.BoardId;

public interface InitializeNewBoard {

    BoardId initializeNewGame();
}
