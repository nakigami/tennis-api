package com.bforbank.tennis_api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class Player {

    private final String name;

    @Setter
    private int points = 0;

    @Setter
    private boolean hasAdvantage = false;

    public void winBall() {
        if (points < 30) {
            points += 15;
        } else if (points == 30) {
            points = 40;
        }
    }

    public boolean isAtDeuce() {
        return points == 40;
    }

    public void resetAdvantage() {
        this.hasAdvantage = false;
    }
}
