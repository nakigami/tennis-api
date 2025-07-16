package com.bforbank.tennis_api.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Match {

    private String id = UUID.randomUUID().toString();

    private Player playerA;
    private Player playerB;
    private MatchStatus status = MatchStatus.ONGOING;
    private String winner;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime date = ZonedDateTime.now(ZoneId.of("Europe/Paris"));


    public Match(Player playerA, Player playerB) {
        this.id = UUID.randomUUID().toString();  // id en String
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public void finish(String winner) {
        this.status = MatchStatus.FINISHED;
        this.winner = winner;
    }
}
