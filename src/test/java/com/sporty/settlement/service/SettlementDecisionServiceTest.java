package com.sporty.settlement.service;

import com.sporty.settlement.dto.BetSettlementStatus;
import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.entity.BetStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SettlementDecisionServiceTest {

    private final SettlementDecisionService decisionService = new SettlementDecisionService();

    @Test
    void shouldReturnWonWhenWinnerIdMatches() {
        Bet bet = buildBet(10L);
        EventOutcome outcome = new EventOutcome(101L, "Football Match", 10L);

        assertThat(decisionService.decide(bet, outcome)).isEqualTo(BetSettlementStatus.WON);
    }

    @Test
    void shouldReturnLostWhenWinnerIdDoesNotMatch() {
        Bet bet = buildBet(99L);
        EventOutcome outcome = new EventOutcome(101L, "Football Match", 10L);

        assertThat(decisionService.decide(bet, outcome)).isEqualTo(BetSettlementStatus.LOST);
    }

    private Bet buildBet(Long winnerId) {
        Bet bet = new Bet();
        bet.setId(1L);
        bet.setUserId(1L);
        bet.setEventId(101L);
        bet.setMarketId(1L);
        bet.setWinnerId(winnerId);
        bet.setAmount(BigDecimal.TEN);
        bet.setStatus(BetStatus.PENDING);
        return bet;
    }
}

