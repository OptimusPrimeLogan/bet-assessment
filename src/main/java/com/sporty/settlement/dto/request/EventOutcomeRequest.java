package com.sporty.settlement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventOutcomeRequest(
        @NotNull(message = "Event ID is required") Long eventId,
        @NotBlank(message = "Event name is required") String eventName,
        @NotNull(message = "Winner ID is required") Long winnerId) {}
