package com.bforbank.tennis_api.infrastructure.adapter.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.ZonedDateTime;

/**
 * Data Transfer Object for exposing match data via the API layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDto {
    private String id;
    private String playerAName;
    private int playerAPoints;
    private boolean playerAHasAdvantage;
    private String playerBName;
    private int playerBPoints;
    private boolean playerBHasAdvantage;
    private String status;
    private String winner;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime date;
}
