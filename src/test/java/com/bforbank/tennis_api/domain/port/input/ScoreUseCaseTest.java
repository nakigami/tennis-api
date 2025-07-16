package com.bforbank.tennis_api.domain.port.input;

import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.domain.model.MatchStatus;
import com.bforbank.tennis_api.domain.model.Player;
import com.bforbank.tennis_api.domain.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreUseCaseTest {

    @Mock
    private ScoreService scoreService;
    @InjectMocks
    private ScoreUseCase scoreUseCase;

    @Test
    void processMatch_shouldReturnMatchWithCorrectWinner() {
        // Given
        String pointsSequence = "AAA";

        Player playerA = new Player("A");
        Player playerB = new Player("B");
        Match mockMatch = new Match(playerA, playerB);
        mockMatch.finish("Player A");

        when(scoreService.computeScore(pointsSequence)).thenReturn(mockMatch);

        // When
        Match result = scoreUseCase.processMatch(pointsSequence);

        // Then
        assertNotNull(result);
        assertEquals(MatchStatus.FINISHED, result.getStatus());
        assertEquals("Player A", result.getWinner());
        verify(scoreService, times(1)).computeScore(pointsSequence);
    }

    @Test
    void getAllMatches_shouldReturnList() {
        when(scoreService.getAllMatches()).thenReturn(java.util.List.of());
        assertNotNull(scoreUseCase.getAllMatches());
        verify(scoreService, times(1)).getAllMatches();
    }

    @Test
    void getMatchById_shouldReturnOptional() {
        var uuid = java.util.UUID.randomUUID();
        Match match = new Match(new Player("A"), new Player("B"));
        when(scoreService.getMatchById(uuid)).thenReturn(Optional.of(match));
        assertEquals(Optional.of(match), scoreUseCase.getMatchById(uuid));
        verify(scoreService, times(1)).getMatchById(uuid);
    }

    @Test
    void processMatch_shouldThrowException_whenInvalidPointsSequence() {
        String invalidSequence = "AXYZ";
        when(scoreService.computeScore(invalidSequence))
                .thenThrow(new com.bforbank.tennis_api.domain.exception.InvalidPointsSequenceException("Invalid points sequence"));
        assertThrows(
                com.bforbank.tennis_api.domain.exception.InvalidPointsSequenceException.class,
                () -> scoreUseCase.processMatch(invalidSequence)
        );
        verify(scoreService, times(1)).computeScore(invalidSequence);
    }

    @Test
    void getMatchById_shouldReturnEmpty_whenNotFound() {
        var uuid = java.util.UUID.randomUUID();
        when(scoreService.getMatchById(uuid)).thenReturn(Optional.empty());
        Optional<Match> result = scoreUseCase.getMatchById(uuid);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scoreService, times(1)).getMatchById(uuid);
    }
}
