package com.msaid.gamelove.service;

import com.msaid.gamelove.config.GameLoveProperties;
import com.msaid.gamelove.dto.GameLoveTrackRequest;
import com.msaid.gamelove.dto.GameLoveTrackResp;
import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.entity.GameStats;
import com.msaid.gamelove.persistence.entity.UserEntity;
import com.msaid.gamelove.persistence.entity.UserGameLoveEntity;
import com.msaid.gamelove.persistence.repository.UserGameLoveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class GameLoveTrackService {
    private final UserGameLoveRepository userGameLoveRepository;
    private final CustomUserDetailService customUserDetailService;
    private final GameService gameService;
    private final GameLoveProperties gameLoveProperties;



    public CompletableFuture<GameLoveTrackResp> trackStatistic(GameLoveTrackRequest request){
        return customUserDetailService.findById(request.getUserId())
                .thenCompose(user -> {
                    return gameService.findById(request.getGameId())
                            .thenApply(game -> Pair.of(user, game));
                })
                .thenCompose(userEntityGameEntityPair ->
                        upsert(userEntityGameEntityPair.getFirst(), userEntityGameEntityPair.getSecond(), request.getLove())
                                .thenApply(userGameLoveEntity -> GameLoveTrackResp.builder()
                                        .userId(userEntityGameEntityPair.getFirst().getId())
                                        .userName(userEntityGameEntityPair.getFirst().getUserName())
                                        .gameId(userEntityGameEntityPair.getSecond().getId())
                                        .gameName(userEntityGameEntityPair.getSecond().getGameName())
                                        .love(request.getLove())
                                        .build()));

    }

    public CompletableFuture<UserGameLoveEntity> upsert(UserEntity userEntity, GameEntity gameEntity, boolean love){
         return userGameLoveRepository.findByUserAndAndGame(userEntity, gameEntity)
                .thenApply(userGameLoveEntityOpt -> {
                    if (userGameLoveEntityOpt.isPresent()){
                        UserGameLoveEntity userGameLoveEntity = userGameLoveEntityOpt.get();
                        if (userGameLoveEntity.isLove() != love) {
                            userGameLoveEntity.setLove(love);
                            userGameLoveEntity = userGameLoveRepository.save(userGameLoveEntity);
                            return userGameLoveEntity;
                        }
                        return userGameLoveEntity;
                    }
                    UserGameLoveEntity newTrack = UserGameLoveEntity.builder()
                            .user(userEntity)
                            .game(gameEntity)
                            .love(love)
                            .build();
                    newTrack = userGameLoveRepository.save(newTrack);
                    return newTrack;
                });
    }


    public CompletableFuture<Page<GameEntity>> getGamesOfUserLoved(String username,Integer page) {
        return userGameLoveRepository.findGamesByUsername(username, PageRequest.of(page, getPageSize()));
    }
    public CompletableFuture<Page<GameEntity>> getGamesOfUserLoved(long userId, Integer page){
        return userGameLoveRepository.findGamesByUserId(userId, PageRequest.of(page, getPageSize()));
    }
    public CompletableFuture<Page<GameStats>> getMostLoved(int top){
        return userGameLoveRepository.getGameStats(PageRequest.of(0, top));
    }

    public int getPageSize(){
        return gameLoveProperties.getPageSize();
    }
}
