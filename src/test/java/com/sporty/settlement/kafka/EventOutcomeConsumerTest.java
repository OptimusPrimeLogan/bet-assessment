package com.sporty.settlement.kafka;

import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.service.SettlementService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventOutcomeConsumerTest {

    @Mock
    private SettlementService settlementService;

    @InjectMocks
    private EventOutcomeConsumer eventOutcomeConsumer;

    @Test
    void shouldDelegateToSettlementService() {
        EventOutcome outcome = new EventOutcome(101L, "Football Match", 10L);

        eventOutcomeConsumer.consume(outcome);

        verify(settlementService).processEventOutcome(outcome);
    }

    @Test
    void shouldIgnoreInvalidPayload() {
        eventOutcomeConsumer.consume(new EventOutcome(null, "Football Match", 10L));

        verify(settlementService, never()).processEventOutcome(any());
    }
}
