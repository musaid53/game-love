package com.msaid.gamelove.persistence.repository;

import com.msaid.gamelove.config.DBThreadPoolExecutorConfig;
import com.msaid.gamelove.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Async(DBThreadPoolExecutorConfig.DEFAULT_DB_EXECUTOR_BEAN)
    @Query(value = "select * from roles where role in (:roles)", nativeQuery = true)
    CompletableFuture<List<RoleEntity>> getAllByName(@Param("roles") Set<String> roles);
}
