package com.sporty.settlement.dto.response;

import com.sporty.settlement.entity.BetStatus;

import java.math.BigDecimal;

public record BetResponse(
        Long id, Long userId, Long eventId, Long marketId, Long winnerId, BigDecimal amount, BetStatus status) {}
