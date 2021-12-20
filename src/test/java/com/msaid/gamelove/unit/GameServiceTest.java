package com.msaid.gamelove.unit;

import com.msaid.gamelove.config.DBThreadPoolExecutor;
import com.msaid.gamelove.config.GameLoveProperties;
import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.repository.GameRepository;
import com.msaid.gamelove.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Spy
    private DBThreadPoolExecutor dbThreadPoolExecutor =
            new DBThreadPoolExecutor(1, 2, "test-thread", 10);
    @Mock
    private GameLoveProperties gameLoveProperties;


    @Test
    void createGame() throws ExecutionException, InterruptedException {
        final String gameName = "Mario";
        final String gameName2 = "Mario2";

        GameEntity game = GameEntity.builder().id(1L).gameName(gameName).build();
        GameEntity game2 = GameEntity.builder().id(2L).gameName(gameName2).build();

        Mockito.when(gameRepository.findByGameName(game.getGameName()))
                .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        Mockito.when(gameRepository.findByGameName(game2.getGameName()))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(game2)));

        Mockito.when(gameRepository.save(ArgumentMatchers.any(GameEntity.class)))
                .thenReturn(game);

        GameEntity saved = gameService.createGame(gameName).get();
        Assertions.assertEquals(saved, game);
        // should warn
        Assertions.assertDoesNotThrow(() -> gameService.createGame(gameName2).get());


    }

    @Test
    void getGameByName() throws ExecutionException, InterruptedException {
        final String gameName = "Mario";
        final String gameName2 = "Mario2";

        GameEntity game = GameEntity.builder().id(1L).gameName(gameName).build();
        GameEntity game2 = GameEntity.builder().id(2L).gameName(gameName2).build();

        Mockito.when(gameRepository.findByGameName(game.getGameName()))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(game)));

        Mockito.when(gameRepository.findByGameName(game2.getGameName()))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(game2)));

        GameEntity saved = gameService.getGameByName(gameName).get();
        GameEntity saved2 = gameService.getGameByName(gameName2).get();
        Assertions.assertEquals(saved, game);
        Assertions.assertEquals(saved2, game2);
    }

    @Test
    void findById() throws ExecutionException, InterruptedException {
        final long id = 1;
        GameEntity game = GameEntity.builder().id(id).gameName("GTA5").build();

        Mockito.when(gameRepository.findById(id))
                .thenReturn(Optional.of(game));
        GameEntity gameReturned = gameService.findById(id).get();
        Assertions.assertEquals(gameReturned, game);
        Assertions.assertThrows(ExecutionException.class, () -> gameService.findById(2).get())
                .getMessage().equals("Game not found");


    }
}
