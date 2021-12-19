package com.msaid.gamelove;

import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.persistence.entity.UserGameLoveEntity;
import com.msaid.gamelove.persistence.repository.GameRepository;
import com.msaid.gamelove.persistence.repository.UserGameLoveRepository;
import com.msaid.gamelove.persistence.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class StatisticTests {
    @Autowired
    WebTestClient client;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    UserGameLoveRepository userGameLoveRepository;

    @BeforeEach
    void setUp() {
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
