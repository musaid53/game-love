package com.msaid.gamelove.integration;

import com.msaid.gamelove.controller.AuthController;
import com.msaid.gamelove.dto.UserRequestDto;
import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.persistence.entity.UserGameLoveEntity;
import com.msaid.gamelove.persistence.repository.GameRepository;
import com.msaid.gamelove.persistence.repository.UserGameLoveRepository;
import com.msaid.gamelove.persistence.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameLoveAppIntegrtionTests {
    private String token;

    @Autowired
    WebTestClient client;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserGameLoveRepository userGameLoveRepository;
    @Autowired
    AuthController authController;

    @BeforeAll
    void setUp() throws ExecutionException, InterruptedException {
//        client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
//         client = MockMvcWebTestClient.bindToController(new GameController(gameService, gameLoveTrackService)).build();
        token = authController.login(UserRequestDto.builder()
                        .username("admin").password("admin")
                        .build()).thenApply(loginResponseResponseEntity -> loginResponseResponseEntity.getBody().getAccessToken())
                .get();
        initDefaultDataForGameLoveTrack();
    }

    @Test
    public void createAndGetGame() throws ExecutionException, InterruptedException {
        final String gameName = "Mario";

        client.put().uri("/game/create")
                .body(Mono.just(gameName), String.class)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isCreated().expectBody(ParameterizedTypeReference.forType(GameEntity.class))
                .value(o -> {
                    Assert.isInstanceOf(GameEntity.class, o);
                    GameEntity g = (GameEntity) o;
                    Assertions.assertThat(gameName).isEqualTo(g.getGameName());
                });
        GameEntity game = gameRepository.findByGameName(gameName).get().get();
        Assertions.assertThat(game.getGameName()).isEqualTo(gameName);

        client.get().uri("/game/games/{gameName}", gameName)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk().expectBody(ParameterizedTypeReference.forType(GameEntity.class))
                .value(o -> {
                    Assert.isInstanceOf(GameEntity.class, o);
                    GameEntity g = (GameEntity) o;
                    Assertions.assertThat(gameName).isEqualTo(g.getGameName());
                });

    }

    @Test
    public void getMostLovedGamesTest(){
        client.get().uri("/game/most-loved-games?top=2")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.content").value(o -> {
                    List<Map> gameStats = (List<Map>) o;
                    Assertions.assertThat(gameStats.get(0).get("loveCount")).isEqualTo(Integer.valueOf(3));
                    Assertions.assertThat(gameStats.get(0).get("gameName")).isEqualTo("Mario1");
                    Assertions.assertThat(gameStats.get(1).get("loveCount")).isEqualTo(Integer.valueOf(2));
                    Assertions.assertThat(gameStats.get(1).get("gameName")).isEqualTo("Mario2");
                });
    }

    @Test
    void getLovedGameByUserId(){

        client.get().uri("/user/loved-games/{userName}", "user1")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk().expectBody().jsonPath("$.content").value(o -> {
                    List<Map> games = (List<Map>) o;
                    Assertions.assertThat(games.size()).isEqualTo(2);
                    Set<String> gameSet = games.stream().map(map -> map.get("gameName").toString())
                            .collect(Collectors.toSet());
                    Assertions.assertThat(gameSet).contains("Mario2")
                            .contains("Mario1")
                            .doesNotContain("Mario3");
                });

    }
    private void initDefaultDataForGameLoveTrack(){
        List<GameEntity> games = List.of(
                GameEntity.builder().gameName("Mario1").build(),
                GameEntity.builder().gameName("Mario2").build(),
                GameEntity.builder().gameName("Mario3").build()
        );
        gameRepository.saveAll(games);
        List<UserEntity> users = List.of(
                UserEntity.builder().userName("user1").build(),
                UserEntity.builder().userName("user2").build(),
                UserEntity.builder().userName("user3").build()
        );
        userRepository.saveAll(users);
        List<UserGameLoveEntity> gameLoves = List.of(
                UserGameLoveEntity.builder().user(users.get(0)).game(games.get(0)).love(true).build(),
                UserGameLoveEntity.builder().user(users.get(1)).game(games.get(0)).love(true).build(),
                UserGameLoveEntity.builder().user(users.get(2)).game(games.get(0)).love(true).build(),
                UserGameLoveEntity.builder().user(users.get(0)).game(games.get(1)).love(true).build(),
                UserGameLoveEntity.builder().user(users.get(1)).game(games.get(1)).love(true).build(),
                UserGameLoveEntity.builder().user(users.get(2)).game(games.get(2)).love(true).build()

        );
        userGameLoveRepository.saveAll(gameLoves);
    }

}
