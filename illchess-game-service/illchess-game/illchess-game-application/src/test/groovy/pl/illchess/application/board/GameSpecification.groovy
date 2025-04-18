package pl.illchess.application.board

import pl.illchess.game.application.game.command.GameManager
import pl.illchess.game.application.game.command.in.CheckGameStateUseCase
import pl.illchess.game.application.game.command.in.CheckLegalityMoveUseCase
import pl.illchess.game.application.game.command.in.EstablishFenStringOfBoardUseCase
import pl.illchess.game.application.game.command.in.JoinOrInitializeNewGameUseCase
import pl.illchess.game.application.game.command.in.MovePieceUseCase
import pl.illchess.game.application.game.command.out.DeleteGame
import pl.illchess.game.application.game.command.out.LoadGame
import pl.illchess.game.application.game.command.out.SaveGame
import spock.lang.Specification

class GameSpecification extends Specification {

    TestGameRepository testGameRepository = new TestGameRepository()
    TestEventPublisher testEventPublisher = new TestEventPublisher()

    LoadGame loadBoard = testGameRepository
    SaveGame saveBoard = testGameRepository
    DeleteGame deleteBoard = testGameRepository

    GameManager boardManager = new GameManager(loadBoard, saveBoard, deleteBoard, testEventPublisher)

    JoinOrInitializeNewGameUseCase joinOrInitializeNewGameUseCase = boardManager
    CheckGameStateUseCase checkIfCheckmateOrStalemateUseCase = boardManager
    CheckLegalityMoveUseCase checkLegalityMoveUseCase = boardManager
    MovePieceUseCase movePieceUseCase = boardManager
    EstablishFenStringOfBoardUseCase establishFenStringOfBoardUseCase = boardManager
}
