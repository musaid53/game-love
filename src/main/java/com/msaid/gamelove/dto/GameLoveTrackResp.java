package com.msaid.gamelove.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@AllArgsConstructor
@NoArgsConstructor
public class GameLoveTrackResp {
    private long userId;
    private String userName;
    private long gameId;
    private String gameName;
    private boolean love;
}
