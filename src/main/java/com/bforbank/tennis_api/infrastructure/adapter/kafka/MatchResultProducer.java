package com.bforbank.tennis_api.infrastructure.adapter.kafka;

import com.bforbank.tennis_api.domain.model.Match;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchResultProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "match-results";

    public void publishFormattedMatchResult(String matchId, String message) {
        try {
            kafkaTemplate.send(TOPIC, matchId, message);
        } catch (Exception e) {
            // log mieux en vrai prod
            System.err.println("Failed to publish match result: " + e.getMessage());
        }
    }
}
