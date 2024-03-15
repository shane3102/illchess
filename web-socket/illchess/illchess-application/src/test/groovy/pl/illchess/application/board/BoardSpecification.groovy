package pl.illchess.application.board

import pl.illchess.application.board.command.BoardManager
import pl.illchess.application.board.command.in.CheckIfCheckmateOrStalemateUseCase
import pl.illchess.application.board.command.in.CheckLegalityMoveUseCase
import pl.illchess.application.board.command.out.LoadBoard
import pl.illchess.application.board.command.out.SaveBoard
import spock.lang.Specification

class BoardSpecification extends Specification {

    TestBoardRepository testBoardRepository = new TestBoardRepository()
    TestEventPublisher testEventPublisher = new TestEventPublisher()

    LoadBoard loadBoard = testBoardRepository
    SaveBoard saveBoard = testBoardRepository

    BoardManager boardManager = new BoardManager(loadBoard, saveBoard, testEventPublisher)

    CheckIfCheckmateOrStalemateUseCase checkIfCheckmateOrStalemateUseCase = boardManager
    CheckLegalityMoveUseCase checkLegalityMoveUseCase = boardManager
}
