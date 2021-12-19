package com.msaid.gamelove.service;

import com.msaid.gamelove.config.GameLoveProperties;
import com.msaid.gamelove.exception.BaseException;
import com.msaid.gamelove.config.DBThreadPoolExecutor;
import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class GameService {
    private final GameRepository gameRepository;
    private final DBThreadPoolExecutor dbThreadPoolExecutor;
    private final GameLoveProperties gameLoveProperties;


    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        log.info("initializing games from properties");
        gameRepository.deleteAll();
        List<GameEntity> defaultGames = gameLoveProperties.getGames().stream()
                .map(gameProperties -> GameEntity.builder().gameName(gameProperties.getName()).build())
                .collect(Collectors.toList());
        gameRepository.saveAll(defaultGames);
        log.info("games created ..");
    }


    public CompletableFuture<GameEntity> createGame( String gameName){
            return gameRepository.findByGameName(gameName)
                    .thenApply(gameEntityOptional -> {
                        if (gameEntityOptional.isEmpty()){
                            // same thread of dbThreadPoolExecutor no need to CompletableFuture.supplyAsyc
                            log.info("game: {} is saving ..", gameName);
                            return gameRepository.save(GameEntity.builder()
                                    .gameName(gameName)
                                    .build());
                        }
                        log.warn("game: {} is exists, returning", gameName);
                        return gameEntityOptional.get();
                    });
    }

    public CompletableFuture<GameEntity> getGameByName(String gameName){
        return gameRepository.findByGameName(gameName)
                .thenApply(gameEntityOpt -> {
                    if (gameEntityOpt.isEmpty())
                        throw new BaseException("Game not Found");

                    return gameEntityOpt.get();
                });
    }

    public CompletableFuture<GameEntity> findById(long gameId){
        return CompletableFuture.supplyAsync(() -> gameRepository.findById(gameId).orElseThrow(() -> new BaseException("Game not found")), dbThreadPoolExecutor);
    }
}
