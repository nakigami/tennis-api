package com.bforbank.tennis_api.infrastructure.adapter.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScoreRequest {
    @NotNull(message = "Score is required")
    @Pattern(regexp = "[ABab]+", message = "Score must only contain characters 'A' or 'B' (case-insensitive)")
    private String score;
}