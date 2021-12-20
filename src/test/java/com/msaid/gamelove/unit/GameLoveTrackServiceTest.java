package com.msaid.gamelove.unit;

import com.msaid.gamelove.config.GameLoveProperties;
import com.msaid.gamelove.dto.GameLoveTrackRequest;
import com.msaid.gamelove.dto.GameLoveTrackResp;
import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.persistence.entity.UserGameLoveEntity;
import com.msaid.gamelove.persistence.repository.UserGameLoveRepository;
import com.msaid.gamelove.service.CustomUserDetailService;
import com.msaid.gamelove.service.GameLoveTrackService;
import com.msaid.gamelove.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GameLoveTrackServiceTest {

    @InjectMocks
    private GameLoveTrackService gameLoveTrackService;

    @Mock private GameService gameService;
    @Mock private UserGameLoveRepository userGameLoveRepository;
    @Mock private CustomUserDetailService customUserDetailService;
    @Mock private GameLoveProperties gameLoveProperties;

    @BeforeEach
    void setUp() {
    }

    @Test
    void trackStatistic() throws ExecutionException, InterruptedException {
        UserEntity userEntity = UserEntity.builder().id(1L).userName("test").build();
        GameEntity gameEntity = GameEntity.builder().id(1L).gameName("test").build();
        UserGameLoveEntity gameLoveEntity = UserGameLoveEntity.builder()
                .game(gameEntity).user(userEntity).love(true).build();

        Mockito.when(customUserDetailService.findById(userEntity.getId()))
                .thenReturn(CompletableFuture.completedFuture(userEntity));
        Mockito.when(gameService.findById(gameEntity.getId()))
                .thenReturn(CompletableFuture.completedFuture(gameEntity));
        Mockito.when(userGameLoveRepository.findByUserAndAndGame(userEntity, gameEntity))
                .thenReturn(CompletableFuture.completedFuture(Optional.empty()));
        Mockito.lenient().when(userGameLoveRepository.save(gameLoveEntity))
                .thenReturn(gameLoveEntity);

        GameLoveTrackRequest gameLoveTrackRequest =
                GameLoveTrackRequest.builder()
                        .userId(userEntity.getId())
                        .gameId(gameEntity.getId())
                        .love(true).build();
        GameLoveTrackResp gameLoveTrackResp = gameLoveTrackService.trackStatistic(gameLoveTrackRequest).get();
        assertEquals(gameLoveTrackResp.getGameId(), gameEntity.getId());
        assertEquals(gameLoveTrackResp.getGameName(), gameEntity.getGameName());
        assertEquals(gameLoveTrackResp.getUserId(), userEntity.getId());
        assertEquals(gameLoveTrackResp.getUserName(), userEntity.getUserName());
        assertTrue(gameLoveTrackResp.isLove());


    }

}