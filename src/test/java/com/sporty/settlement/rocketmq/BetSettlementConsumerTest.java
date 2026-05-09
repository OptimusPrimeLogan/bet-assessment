package com.sporty.settlement.rocketmq;

import com.sporty.settlement.dto.BetSettlement;
import com.sporty.settlement.dto.BetSettlementStatus;
import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.entity.BetStatus;
import com.sporty.settlement.repository.BetRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetSettlementConsumerTest {

    @Mock
    private BetRepository betRepository;

    @InjectMocks
    private BetSettlementConsumer betSettlementConsumer;

    @Test
    void shouldUpdateBetStatusToWon() {
        Bet bet = buildBet(1L, BetStatus.PENDING);
        when(betRepository.findById(1L)).thenReturn(Optional.of(bet));

        betSettlementConsumer.onMessage(new BetSettlement(1L, 1L, 101L, 10L, BigDecimal.TEN, BetSettlementStatus.WON));

        ArgumentCaptor<Bet> captor = ArgumentCaptor.forClass(Bet.class);
        verify(betRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(BetStatus.WON);
    }

    @Test
    void shouldUpdateBetStatusToLost() {
        Bet bet = buildBet(2L, BetStatus.PENDING);
        when(betRepository.findById(2L)).thenReturn(Optional.of(bet));

        betSettlementConsumer.onMessage(new BetSettlement(2L, 1L, 101L, 99L, BigDecimal.TEN, BetSettlementStatus.LOST));

        ArgumentCaptor<Bet> captor = ArgumentCaptor.forClass(Bet.class);
        verify(betRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(BetStatus.LOST);
    }

    @Test
    void shouldNotSaveWhenBetNotFound() {
        when(betRepository.findById(99L)).thenReturn(Optional.empty());

        betSettlementConsumer.onMessage(new BetSettlement(99L, 1L, 101L, 10L, BigDecimal.TEN, BetSettlementStatus.WON));

        verify(betRepository, never()).save(any());
    }

    private Bet buildBet(Long id, BetStatus status) {
        Bet bet = new Bet();
        bet.setId(id);
        bet.setUserId(1L);
        bet.setEventId(101L);
        bet.setMarketId(1L);
        bet.setWinnerId(10L);
        bet.setAmount(BigDecimal.TEN);
        bet.setStatus(status);
        return bet;
    }
}
