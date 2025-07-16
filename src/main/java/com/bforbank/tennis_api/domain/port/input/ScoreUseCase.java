package com.bforbank.tennis_api.domain.port.input;

import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.domain.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScoreUseCase {

    private final ScoreService scoreService;

    /**
     * Processes the sequence of points and returns the resulting Match object.
     *
     * @param pointsSequence String containing 'A' or 'B' for each point.
     * @return The resulting Match after processing.
     */
    public Match processMatch(String pointsSequence) {
        return scoreService.computeScore(pointsSequence);
    }

    public List<Match> getAllMatches() {
        return scoreService.getAllMatches();
    }

    public Optional<Match> getMatchById(UUID id) {
        return scoreService.getMatchById(id);
    }
}
