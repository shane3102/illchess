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
    Optional<Game> loadGame(GameId gameId) {
        return Optional.ofNullable(repo.get(gameId))
    }

    @Override
    Optional<Game> loadGameWithoutPlayer() {
        return repo.values()
            .stream()
            .filter {it.gameInfo().blackPlayer() == null}
            .findFirst()
    }

    @Override
    Optional<Game> loadGameByUsername(Username username) {
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
        def gameId = savedBoard.gameId() == null ? new GameId(UUID.randomUUID()) : savedBoard.gameId()
        repo.put(gameId, savedBoard)
        gameId
    }

    @Override
    void deleteBoard(GameId gameId) {
        repo.remove(gameId)
    }
}
