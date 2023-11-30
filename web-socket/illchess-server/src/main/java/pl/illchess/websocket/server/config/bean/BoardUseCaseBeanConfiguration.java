package pl.illchess.websocket.server.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.illchess.application.board.command.BoardManager;
import pl.illchess.application.board.command.in.InitializeNewBoardUseCase;
import pl.illchess.application.board.command.in.MovePieceUseCase;
import pl.illchess.application.board.command.out.LoadBoard;
import pl.illchess.application.board.command.out.SaveBoard;
import pl.illchess.application.commons.command.in.PublishEvent;

@Configuration
public class BoardUseCaseBeanConfiguration {

    @Bean
    public MovePieceUseCase movePieceUseCase(
          LoadBoard loadBoard,
          SaveBoard saveBoard,
          PublishEvent eventPublisher
    ) {
        return new BoardManager(
                loadBoard,
                saveBoard,
                eventPublisher
        );
    }

    @Bean
    public InitializeNewBoardUseCase initializeNewBoardUseCase(
            LoadBoard loadBoard,
            SaveBoard saveBoard,
            PublishEvent eventPublisher
    ) {
        return new BoardManager(
                loadBoard,
                saveBoard,
                eventPublisher
        );
    }
}
