package com.sporty.settlement.kafka;

import com.sporty.settlement.dto.EventOutcome;

public interface EventOutcomePublisher {
    void publish(EventOutcome outcome);
}
