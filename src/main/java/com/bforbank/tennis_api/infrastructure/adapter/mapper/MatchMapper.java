package com.bforbank.tennis_api.infrastructure.adapter.mapper;

import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.infrastructure.adapter.controller.MatchDto;

/**
 * Mapper for converting Match domain objects to MatchDto for the API layer.
 */
public class MatchMapper {
    private MatchMapper() {}

    public static MatchDto toDto(Match match) {
        if (match == null) return null;
        return new MatchDto(
                match.getId(),
                match.getPlayerA().getName(),
                match.getPlayerA().getPoints(),
                match.getPlayerA().isHasAdvantage(),
                match.getPlayerB().getName(),
                match.getPlayerB().getPoints(),
                match.getPlayerB().isHasAdvantage(),
                match.getStatus().name(),
                match.getWinner(),
                match.getDate()
        );
    }
}
