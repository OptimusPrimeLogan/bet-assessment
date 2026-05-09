package com.sporty.settlement.service;

import com.sporty.settlement.dto.BetSettlementStatus;
import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.entity.BetStatus;
import com.sporty.settlement.repository.BetRepository;
import com.sporty.settlement.rocketmq.BetSettlementDispatcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private BetSettlementDispatcher betSettlementDispatcher;

    @Mock
    private BetSettlementDecision betSettlementDecision;

    @InjectMocks
    private SettlementService settlementService;

    @Test
    void shouldDispatchNothingWhenNoPendingBetsFound() {
        when(betRepository.findByEventIdAndStatus(99L, BetStatus.PENDING)).thenReturn(List.of());

        settlementService.processEventOutcome(new EventOutcome(99L, "Unknown", 1L));

        verify(betSettlementDispatcher, never()).send(any());
    }

    @Test
    void shouldMarkBetAsWonWhenWinnerMatches() {
        Bet bet = buildBet(1L, 101L, 10L);
        EventOutcome outcome = new EventOutcome(101L, "Match", 10L);
        when(betRepository.findByEventIdAndStatus(101L, BetStatus.PENDING)).thenReturn(List.of(bet));
        when(betSettlementDecision.decide(bet, outcome)).thenReturn(BetSettlementStatus.WON);

        settlementService.processEventOutcome(outcome);

        verify(betSettlementDispatcher).send(argThat(s -> s.status() == BetSettlementStatus.WON));
    }

    @Test
    void shouldMarkBetAsLostWhenWinnerDoesNotMatch() {
        Bet bet = buildBet(2L, 101L, 99L);
        EventOutcome outcome = new EventOutcome(101L, "Match", 10L);
        when(betRepository.findByEventIdAndStatus(101L, BetStatus.PENDING)).thenReturn(List.of(bet));
        when(betSettlementDecision.decide(bet, outcome)).thenReturn(BetSettlementStatus.LOST);

        settlementService.processEventOutcome(outcome);

        verify(betSettlementDispatcher).send(argThat(s -> s.status() == BetSettlementStatus.LOST));
    }

    @Test
    void shouldSettleMixedBetsCorrectly() {
        Bet wonBet = buildBet(1L, 101L, 10L);
        Bet lostBet = buildBet(2L, 101L, 99L);
        EventOutcome outcome = new EventOutcome(101L, "Match", 10L);
        when(betRepository.findByEventIdAndStatus(101L, BetStatus.PENDING)).thenReturn(List.of(wonBet, lostBet));
        when(betSettlementDecision.decide(wonBet, outcome)).thenReturn(BetSettlementStatus.WON);
        when(betSettlementDecision.decide(lostBet, outcome)).thenReturn(BetSettlementStatus.LOST);

        settlementService.processEventOutcome(outcome);

        verify(betSettlementDispatcher)
                .send(argThat(
                        s -> s.status() == BetSettlementStatus.WON && s.betId().equals(1L)));
        verify(betSettlementDispatcher)
                .send(argThat(
                        s -> s.status() == BetSettlementStatus.LOST && s.betId().equals(2L)));
    }

    @Test
    void getBetsByEventId_shouldReturnBets() {
        Bet bet = buildBet(1L, 101L, 10L);
        when(betRepository.findByEventId(101L)).thenReturn(List.of(bet));

        List<Bet> result = settlementService.getBetsByEventId(101L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(1L);
    }

    @Test
    void getBetsByEventId_shouldReturnEmptyList_whenNoBetsExist() {
        when(betRepository.findByEventId(999L)).thenReturn(Collections.emptyList());

        assertThat(settlementService.getBetsByEventId(999L)).isEmpty();
    }

    private Bet buildBet(Long id, Long eventId, Long winnerId) {
        Bet bet = new Bet();
        bet.setId(id);
        bet.setUserId(1L);
        bet.setEventId(eventId);
        bet.setMarketId(1L);
        bet.setWinnerId(winnerId);
        bet.setAmount(BigDecimal.TEN);
        bet.setStatus(BetStatus.PENDING);
        return bet;
    }
}
