package com.bforbank.tennis_api.domain.service;

import com.bforbank.tennis_api.domain.exception.InvalidPointsSequenceException;
import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.domain.model.MatchStatus;
import com.bforbank.tennis_api.domain.model.Player;
import com.bforbank.tennis_api.domain.port.output.MatchRepository;
import com.bforbank.tennis_api.infrastructure.adapter.kafka.MatchResultProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreService {

    private final MatchRepository matchRepository;
    private final MatchResultProducer matchResultProducer;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getMatchById(UUID id) {
        return matchRepository.findById(String.valueOf(id));
    }

    /**
     * This function takes a sequence of points and returns the match log as text.
     *
     * @param pointsSequence Sequence of points ('A' or 'B')
     * @return Formatted match log
     */
    public String generateMatchLog(String pointsSequence) {
        if (pointsSequence == null || !pointsSequence.matches("[ABab]+")) {
            throw new InvalidPointsSequenceException("Invalid points sequence: only characters 'A' or 'B' are allowed (case-insensitive). Example: 'BAAABABA'.");
        }

        if (pointsSequence.length() > 300) {
            throw new InvalidPointsSequenceException("Sequence too long (max 300 points)");
        }
        
        Match match = new Match(new Player("A"), new Player("B"));
        StringBuilder result = new StringBuilder();

        pointsSequence.chars()
            .mapToObj(c -> Character.toUpperCase((char) c))
            .takeWhile(c -> match.getStatus() != MatchStatus.FINISHED)
            .forEach(c -> {
                applyPoint(match, c);
                if (match.getStatus() == MatchStatus.FINISHED) {
                    result.append(match.getWinner()).append(" wins the game");
                } else {
                    String scoreLine = String.format("Player A : %s / Player B : %s",
                        formatScore(match.getPlayerA()),
                        formatScore(match.getPlayerB()));
                    result.append(scoreLine).append("\n");
                }
            });

        log.info("----- Match Result --------\n{}", result);
        
        return result.toString();
    }

    /**
     * Calculates and persists the score based on the sequence of points.
     * Publishes the formatted result and returns the Match object.
     *
     * @param pointsSequence Sequence of points ('A' or 'B')
     * @return The resulting Match object
     */
    public Match computeScore(String pointsSequence) {
        Match match = new Match(new Player("A"), new Player("B"));

        pointsSequence.chars()
                .mapToObj(c -> (char) c)
                .takeWhile(c -> match.getStatus() != MatchStatus.FINISHED)
                .forEach(c -> applyPoint(match, c));

        matchRepository.save(match);

        String matchLog = generateMatchLog(pointsSequence);
        matchResultProducer.publishFormattedMatchResult(match.getId(), matchLog);

        return match;
    }

    /**
     * Applies a point to the current match based on the scorer's character ('A' or 'B').
     * Handles both deuce and normal scoring situations.
     * If the match is already finished, this method does nothing.
     *
     * @param match The match object to update
     * @param c     The character representing the player who won the point ('A' or 'B')
     */
    private void applyPoint(Match match, char c) {
        if (match.getStatus() == MatchStatus.FINISHED) return;

        Player scorer = (c == 'A' || c == 'a') ? match.getPlayerA() : match.getPlayerB();
        Player opponent = (scorer == match.getPlayerA()) ? match.getPlayerB() : match.getPlayerA();

        if (isDeuce(match.getPlayerA(), match.getPlayerB())) {
            handleDeuce(match, scorer, opponent);
        } else {
            handleNormalPoint(match, scorer);
        }
    }

    /**
     * Handles the logic when the match is in a deuce situation.
     * - If neither player has advantage, gives advantage to the scorer.
     * - If the scorer already has advantage, finishes the match in their favor.
     * - If the opponent has advantage, removes it.
     *
     * @param match    The match object
     * @param scorer   The player who won the point
     * @param opponent The opposing player
     */
    private void handleDeuce(Match match, Player scorer, Player opponent) {
        if (!scorer.isHasAdvantage() && !opponent.isHasAdvantage()) {
            scorer.setHasAdvantage(true);
        } else if (scorer.isHasAdvantage()) {
            match.finish("Player " + scorer.getName());
        } else {
            opponent.setHasAdvantage(false);
        }
    }

    /**
     * Handles the logic when the match is not in a deuce situation.
     * - If the scorer has less than 40 points, increments their points.
     * - If the scorer has 40 points, finishes the match in their favor.
     *
     * @param match  The match object
     * @param scorer The player who won the point
     */
    private void handleNormalPoint(Match match, Player scorer) {
        if (scorer.getPoints() < 40) {
            scorer.winBall();
        } else {
            match.finish("Player " + scorer.getName());
        }
    }

    /**
     * Checks if both players are at deuce (both have 40 points).
     *
     * @param a First player
     * @param b Second player
     * @return True if both are at deuce, false otherwise
     */
    private boolean isDeuce(Player a, Player b) {
        return a.getPoints() == 40 && b.getPoints() == 40;
    }

    /**
     * Formats the score for display.
     *
     * @param player Player whose score to format
     * @return String representation of the player's score
     */
    private String formatScore(Player player) {
        if (player.isHasAdvantage()) return "Advantage";
        return String.valueOf(player.getPoints());
    }
}
