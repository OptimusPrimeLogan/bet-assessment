package com.sporty.settlement.kafka;

import com.sporty.settlement.dto.EventOutcome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventOutcomeProducer implements EventOutcomePublisher {

    private final String topic;
    private final KafkaTemplate<String, EventOutcome> kafkaTemplate;

    public EventOutcomeProducer(
            KafkaTemplate<String, EventOutcome> kafkaTemplate,
            @Value("${app.kafka.topics.event-outcomes:event-outcomes}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(EventOutcome outcome) {
        log.info("Publishing event outcome to Kafka: {}", outcome);
        kafkaTemplate.send(topic, String.valueOf(outcome.eventId()), outcome);
    }
}
