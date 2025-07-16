package com.bforbank.tennis_api.infrastructure.adapter.controller;

import com.bforbank.tennis_api.infrastructure.adapter.requests.ScoreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void pingEndpoint_shouldReturnHello() throws Exception {
        String response = mockMvc.perform(get("/api/v1/matches/ping"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("Hello Tennis API!", response);
    }

    @Test
    void createMatchFromScore_shouldReturnMatch() throws Exception {
        ScoreRequest request = new ScoreRequest();
        request.setScore("ABABABAA");
        mockMvc.perform(post("/api/v1/matches/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.playerAName").value("A"))
                .andExpect(jsonPath("$.playerAPoints").value(40))
                .andExpect(jsonPath("$.playerAHasAdvantage").isBoolean())
                .andExpect(jsonPath("$.playerBName").value("B"))
                .andExpect(jsonPath("$.playerBPoints").value(40))
                .andExpect(jsonPath("$.playerBHasAdvantage").isBoolean())
                .andExpect(jsonPath("$.status").value("FINISHED"))
                .andExpect(jsonPath("$.winner").value("Player A"))
                .andExpect(jsonPath("$.date").exists());
    }

    @Test
    void createMatchFromScore_shouldReturn400_whenScoreIsNull() throws Exception {
        ScoreRequest request = new ScoreRequest();
        request.setScore(null);
        mockMvc.perform(post("/api/v1/matches/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/v1/matches/score"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Score is required")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createMatchFromScore_shouldReturn400_whenScoreIsEmpty() throws Exception {
        ScoreRequest request = new ScoreRequest();
        request.setScore("");
        mockMvc.perform(post("/api/v1/matches/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/v1/matches/score"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Score must only contain characters 'A' or 'B'")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createMatchFromScore_shouldReturn400_whenScoreIsInvalidFormat() throws Exception {
        ScoreRequest request = new ScoreRequest();
        request.setScore("ABACD");
        mockMvc.perform(post("/api/v1/matches/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/v1/matches/score"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Score must only contain characters 'A' or 'B'")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createMatchFromScore_shouldReturn400_whenScoreIsTooLong() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 301; i++) sb.append("A");
        ScoreRequest request = new ScoreRequest();
        request.setScore(sb.toString());
        mockMvc.perform(post("/api/v1/matches/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/v1/matches/score"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Sequence too long")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getAllMatches_shouldReturnList() throws Exception {
        String jsonResponse = mockMvc.perform(get("/api/v1/matches"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        MatchDto[] matches = objectMapper.readValue(jsonResponse, MatchDto[].class);
        assertNotNull(matches);
    }

    @Test
    void getMatchById_shouldReturn404IfNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/matches/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound());
    }
}
