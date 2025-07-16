package com.bforbank.tennis_api.domain.service;

import com.bforbank.tennis_api.domain.exception.InvalidPointsSequenceException;
import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.domain.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreServiceTest {
    private ScoreService scoreService;

    @BeforeEach
    void setUp() {
        scoreService = new ScoreService(null, null);
    }

    @Test
    void generateMatchLog_shouldThrow_whenNull() {
        assertThrows(InvalidPointsSequenceException.class, () -> scoreService.generateMatchLog(null));
    }

    @Test
    void generateMatchLog_shouldThrow_whenEmpty() {
        assertThrows(InvalidPointsSequenceException.class, () -> scoreService.generateMatchLog(""));
    }

    @Test
    void generateMatchLog_shouldThrow_whenInvalidFormat() {
        assertThrows(InvalidPointsSequenceException.class, () -> scoreService.generateMatchLog("ABCD"));
    }

    @Test
    void generateMatchLog_shouldThrow_whenTooLong() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 301; i++) sb.append("A");
        assertThrows(InvalidPointsSequenceException.class, () -> scoreService.generateMatchLog(sb.toString()));
    }

    @Test
    void generateMatchLog_shouldReturnLog_whenValid() {
        String log = scoreService.generateMatchLog("AAABBBABAB");
        assertNotNull(log);
        assertTrue(log.contains("Player A"));
        assertTrue(log.contains("Player B"));
    }
}
