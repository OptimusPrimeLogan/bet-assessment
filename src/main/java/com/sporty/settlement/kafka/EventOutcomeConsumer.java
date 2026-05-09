package com.sporty.settlement.kafka;

import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOutcomeConsumer implements EventOutcomeHandler {

    private final SettlementService settlementService;

    @Override
    @KafkaListener(topics = "${app.kafka.topics.event-outcomes:event-outcomes}")
    public void consume(EventOutcome outcome) {
        if (outcome == null || outcome.eventId() == null || outcome.winnerId() == null) {
            log.warn("Skipping invalid event outcome from Kafka: {}", outcome);
            return;
        }
        log.info("Received event outcome from Kafka: {}", outcome);
        settlementService.processEventOutcome(outcome);
    }
}
