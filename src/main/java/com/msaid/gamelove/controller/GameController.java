package com.msaid.gamelove.controller;

import com.msaid.gamelove.persistence.entity.GameEntity;
import com.msaid.gamelove.persistence.entity.GameStats;
import com.msaid.gamelove.service.GameLoveTrackService;
import com.msaid.gamelove.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
@Log4j2
@Validated
public class GameController {
    private final GameService gameService;
    private final GameLoveTrackService gameLoveTrackService;


    //create game
    @PutMapping("/create")
    @ApiOperation("Create a new game")
    public CompletableFuture<ResponseEntity<GameEntity>> createGame(@RequestBody @Valid
                                                                    @NotBlank(message = "game Name Cannot be blank")
                                                                    @Size(max = 10, message = "gameName cannot bigger than 10 chars") String gameName) {
        return gameService.createGame(gameName)
                .thenApply(gameEntity -> ResponseEntity
                        .created(URI.create(String.format("/game/%s", gameName))).body(gameEntity));
    }

    //get game by name
    @GetMapping("/games/{gameName}")
    @ApiOperation("Get a game by name")
    public CompletableFuture<ResponseEntity<GameEntity>> getGameByName(@PathVariable("gameName") @Valid @NotBlank(message = "game Name Cannot be blank")
                                                                       @Size(max = 10, message = "gameName cannot bigger than 10 chars") String gameName) {
        return gameService.getGameByName(gameName)
                .thenApply(ResponseEntity::ok);
    }

    // get game statistic
    @GetMapping("/most-loved-games")
    @ApiOperation("Get a most loved game")
    public CompletableFuture<ResponseEntity<Page<GameStats>>> getMostLoved(@RequestParam(value = "top", required = false, defaultValue = "5")
                                                                           @Valid @Max(value = 10, message = "max top value can be 10")
                                                                           @Min(value = 2, message = "min top value can be 2") Integer top) {
        return gameLoveTrackService.getMostLoved(top)
                .thenApply(ResponseEntity::ok);
    }


}
