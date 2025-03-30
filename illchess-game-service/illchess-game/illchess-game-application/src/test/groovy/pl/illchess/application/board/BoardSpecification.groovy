package pl.illchess.application.board

import pl.illchess.game.application.board.command.BoardManager
import pl.illchess.game.application.board.command.in.CheckBoardStateUseCase
import pl.illchess.game.application.board.command.in.CheckLegalityMoveUseCase
import pl.illchess.game.application.board.command.in.EstablishFenStringOfBoardUseCase
import pl.illchess.game.application.board.command.in.JoinOrInitializeNewGameUseCase
import pl.illchess.game.application.board.command.in.MovePieceUseCase
import pl.illchess.game.application.board.command.out.DeleteBoard
import pl.illchess.game.application.board.command.out.LoadBoard
import pl.illchess.game.application.board.command.out.SaveBoard
import spock.lang.Specification

class BoardSpecification extends Specification {

    TestBoardRepository testBoardRepository = new TestBoardRepository()
    TestEventPublisher testEventPublisher = new TestEventPublisher()

    LoadBoard loadBoard = testBoardRepository
    SaveBoard saveBoard = testBoardRepository
    DeleteBoard deleteBoard = testBoardRepository

    BoardManager boardManager = new BoardManager(loadBoard, saveBoard, deleteBoard, testEventPublisher)

    JoinOrInitializeNewGameUseCase joinOrInitializeNewGameUseCase = boardManager
    CheckBoardStateUseCase checkIfCheckmateOrStalemateUseCase = boardManager
    CheckLegalityMoveUseCase checkLegalityMoveUseCase = boardManager
    MovePieceUseCase movePieceUseCase = boardManager
    EstablishFenStringOfBoardUseCase establishFenStringOfBoardUseCase = boardManager
}
