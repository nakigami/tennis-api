package com.bforbank.tennis_api.infrastructure.adapter.controller;

import com.bforbank.tennis_api.domain.model.Match;
import com.bforbank.tennis_api.domain.port.input.ScoreUseCase;
import com.bforbank.tennis_api.infrastructure.adapter.mapper.MatchMapper;
import com.bforbank.tennis_api.infrastructure.adapter.requests.ScoreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
@Tag(name = "Match API", description = "API to manage tennis matches")
@Slf4j
public class MatchController {

    private final ScoreUseCase scoreUseCase;


    @Operation(summary = "Ping endpoint", description = "Returns a simple message to verify the API is running.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API is running", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/ping")
    public String hello() {
        return "Hello Tennis API!";
    }

    @Operation(summary = "Create a match from a score sequence", description = "Creates a new match by processing a sequence of points and returns the resulting match object.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match created", content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping(value = "/score")
    public ResponseEntity<MatchDto> createMatchFromScore(@RequestBody @Valid @Parameter(description = "Score request body", required = true) ScoreRequest request) {
        log.info("Received score: " + request.getScore());
        Match match = scoreUseCase.processMatch(request.getScore());
        return ResponseEntity.ok(MatchMapper.toDto(match));
    }

    @Operation(summary = "Get the list of all past matches", description = "Retrieves all matches that have been played.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of matches", content = @Content(schema = @Schema(implementation = MatchDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        List<MatchDto> matches = scoreUseCase.getAllMatches().stream().map(MatchMapper::toDto).toList();
        return ResponseEntity.ok(matches);
    }

    @Operation(summary = "Get a match by its ID", description = "Retrieves a match by its unique identifier.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match found", content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "404", description = "Match not found", content = @Content)
    })
    @Parameters({
        @Parameter(name = "id", description = "Unique identifier of the match", required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable String id) {
        return scoreUseCase.getMatchById(UUID.fromString(id))
                .map(MatchMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
