package com.sporty.settlement.dto.response;

public record EventOutcomeResponse(Long eventId, String eventName, Long winnerId, String message) {}
