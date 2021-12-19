package com.msaid.gamelove.controller;

import com.msaid.gamelove.dto.GameLoveTrackRequest;
import com.msaid.gamelove.dto.GameLoveTrackResp;
import com.msaid.gamelove.service.GameLoveTrackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/stats")
public class StatisticController {
    private final GameLoveTrackService gameLoveTrackService;

    //create a new entry by feeding it with the following:
    //
    //    The player that loved the game.
    //    The game it loved.
    @PostMapping("/track")
    public CompletableFuture<ResponseEntity<GameLoveTrackResp>> track(@RequestBody @Valid GameLoveTrackRequest request){
        return gameLoveTrackService.trackStatistic(request)
                .thenApply(ResponseEntity::ok);
    }

}
