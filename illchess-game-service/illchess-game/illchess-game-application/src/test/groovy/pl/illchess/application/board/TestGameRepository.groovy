package pl.illchess.application.board

import pl.illchess.game.application.game.command.out.DeleteGame
import pl.illchess.game.application.game.command.out.LoadGame
import pl.illchess.game.application.game.command.out.SaveGame
import pl.illchess.game.domain.game.model.Game
import pl.illchess.game.domain.game.model.GameId
import pl.illchess.game.domain.game.model.state.player.Username

class TestGameRepository implements LoadGame, SaveGame, DeleteGame {

    Map<GameId, Game> repo = new HashMap<>()

    @Override
    Optional<Game> loadBoard(GameId boardId) {
        return Optional.ofNullable(repo.get(boardId))
    }

    @Override
    Optional<Game> loadBoardWithoutPlayer() {
        return repo.values()
            .stream()
            .filter {it.gameInfo().blackPlayer() == null}
            .findFirst()
    }

    @Override
    Optional<Game> loadBoardByUsername(Username username) {
        return repo.values()
                .stream()
                .filter {
                    (it.gameInfo().blackPlayer() !=  null && it.gameInfo().blackPlayer().username() == username)
                            || it.gameInfo().whitePlayer().username() == username
                }
                .findFirst()
    }

    @Override
    GameId saveBoard(Game savedBoard) {
        def boardId = savedBoard.gameId() == null ? new GameId(UUID.randomUUID()) : savedBoard.gameId()
        repo.put(boardId, savedBoard)
        boardId
    }

    @Override
    void deleteBoard(GameId boardId) {
        repo.remove(boardId)
    }
}
