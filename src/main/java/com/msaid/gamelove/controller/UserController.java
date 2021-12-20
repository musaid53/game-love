package com.msaid.gamelove.controller;

import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.service.GameLoveTrackService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/user")
@Validated
public class UserController {
    private final GameLoveTrackService gameLoveTrackService;


    @GetMapping("/loved-games-by-id/{userId}")
    @ApiOperation("Get a users loved games by userId")
    public CompletableFuture<ResponseEntity<Page<GameEntity>>> getAllGamesPlayerLoved (@PathVariable("userId") Long userId,
                                                                                       @RequestParam(value = "page", required = false, defaultValue = "0")
                                                                                       @Min(value = 0, message = "ming page must be 0") Integer page){
        return gameLoveTrackService.getGamesOfUserLoved(userId, page)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/loved-games/{userName}")
    @ApiOperation("Get a users loved games by userName")
    public CompletableFuture<ResponseEntity<Page<GameEntity>>> getAllGamesPlayerLoved (@PathVariable("userName") String userName,
                                                                                       @RequestParam(value = "page", required = false, defaultValue = "0")
                                                                                       @Min(value = 0, message = "ming page must be 0") Integer page){

        return gameLoveTrackService.getGamesOfUserLoved(userName, page)
                .thenApply(ResponseEntity::ok);

    }
}
