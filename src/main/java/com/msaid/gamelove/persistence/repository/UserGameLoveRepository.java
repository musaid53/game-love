package com.msaid.gamelove.persistence.repository;

import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.entity.GameStats;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.persistence.entity.UserGameLoveEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.msaid.gamelove.config.DBThreadPoolExecutorConfig.DEFAULT_DB_EXECUTOR_BEAN;

@Repository
public interface UserGameLoveRepository extends JpaRepository<UserGameLoveEntity, Long> {

    @Async(DEFAULT_DB_EXECUTOR_BEAN)
    CompletableFuture<Optional<UserGameLoveEntity>> findByUserAndAndGame(UserEntity user, GameEntity game);

    @Query("select gl.game from user_game_love gl where gl.user.userName = :userName and gl.love = true")
    @Async(DEFAULT_DB_EXECUTOR_BEAN)
    CompletableFuture<Page<GameEntity>> findGamesByUsername(@Param("userName") String userName, Pageable pageable);

    @Query("select gl.game from user_game_love gl where gl.user.id = :userId and gl.love = true")
    @Async(DEFAULT_DB_EXECUTOR_BEAN)
    CompletableFuture<Page<GameEntity>> findGamesByUserId(@Param("userId") long userId, Pageable pageable);

    @Async(DEFAULT_DB_EXECUTOR_BEAN)
    @Query(value = "select  gl.game_id as gameId, g.game_name as gameName, count(g.id) as loveCount " +
            "from user_game_love gl inner join games g on g.id = gl.game_id where gl.love is true group by gl.game_id, g.game_name order by count(g.id) desc",
            countQuery = "select count(*) from (select  gl.game_id as gameId, g.game_name, count(g.id) as loveCount " +
                    "from user_game_love gl inner join games g on g.id = gl.game_id where gl.love is true group by gl.game_id, g.game_name)",
            nativeQuery = true)
    CompletableFuture<Page<GameStats>> getGameStats(Pageable pageable);
//
//    @Query(value = "select gl.game_id as gameId, count(gl.game_id) as loveCount " +
//            "from user_game_love gl inner join games g on g.id = gl.game_id  where gl.love is true group by gl.game_id", nativeQuery = true)
//    List<GameStats> getA();

}
