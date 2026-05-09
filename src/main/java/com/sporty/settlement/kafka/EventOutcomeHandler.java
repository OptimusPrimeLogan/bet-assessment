package com.sporty.settlement.kafka;

import com.sporty.settlement.dto.EventOutcome;

public interface EventOutcomeHandler {
    void consume(EventOutcome outcome);
}
