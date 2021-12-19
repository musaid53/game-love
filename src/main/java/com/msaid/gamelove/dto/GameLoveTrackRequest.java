package com.msaid.gamelove.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class GameLoveTrackRequest {
    @NotNull(message = "userId must be set")
    private Long userId;
    @NotNull(message = "gameId must be set")
    private Long gameId;
    @NotNull(message = "you must set love situation")
    private Boolean love;
}
