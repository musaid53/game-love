package com.msaid.gamelove.persistence.repository;

import com.msaid.gamelove.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.msaid.gamelove.config.DBThreadPoolExecutorConfig.DEFAULT_DB_EXECUTOR_BEAN;

public interface GameRepository extends JpaRepository<GameEntity, Long> {

    @Async(DEFAULT_DB_EXECUTOR_BEAN)
    CompletableFuture<Optional<GameEntity>> findByGameName(String gameName);

}
