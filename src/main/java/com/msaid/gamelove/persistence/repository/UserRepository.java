package com.msaid.gamelove.persistence.repository;

import com.msaid.gamelove.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.msaid.gamelove.config.DBThreadPoolExecutorConfig.DEFAULT_DB_EXECUTOR_BEAN;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Async(DEFAULT_DB_EXECUTOR_BEAN)
    CompletableFuture<Optional<UserEntity>> findByUserName(String username);
}
