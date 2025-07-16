package com.bforbank.tennis_api.domain.port.output;

import com.bforbank.tennis_api.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {

    Match save(Match match);

    Optional<Match> findById(String matchId);

    List<Match> findAll();

    void deleteById(String matchId);
}