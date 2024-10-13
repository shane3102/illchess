package pl.illchess.application.board

import pl.illchess.application.board.command.BoardManager
import pl.illchess.application.board.command.in.*
import pl.illchess.application.board.command.out.DeleteBoard
import pl.illchess.application.board.command.out.LoadBoard
import pl.illchess.application.board.command.out.SaveBoard
import spock.lang.Specification

class BoardSpecification extends Specification {

    TestBoardRepository testBoardRepository = new TestBoardRepository()
    TestEventPublisher testEventPublisher = new TestEventPublisher()

    LoadBoard loadBoard = testBoardRepository
    SaveBoard saveBoard = testBoardRepository
    DeleteBoard deleteBoard = testBoardRepository

    BoardManager boardManager = new BoardManager(loadBoard, saveBoard, deleteBoard, testEventPublisher)

    JoinOrInitializeNewGameUseCase joinOrInitializeNewGameUseCase = boardManager
    CheckIfCheckmateOrStalemateUseCase checkIfCheckmateOrStalemateUseCase = boardManager
    CheckLegalityMoveUseCase checkLegalityMoveUseCase = boardManager
    MovePieceUseCase movePieceUseCase = boardManager
    EstablishFenStringOfBoardUseCase establishFenStringOfBoardUseCase = boardManager
}
