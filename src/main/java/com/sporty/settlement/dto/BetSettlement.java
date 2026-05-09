package com.sporty.settlement.dto;

import java.math.BigDecimal;

public record BetSettlement(
        Long betId, Long userId, Long eventId, Long winnerId, BigDecimal amount, BetSettlementStatus status) {}
