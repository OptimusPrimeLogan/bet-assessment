package com.sporty.settlement.kafka;

import com.sporty.settlement.dto.EventOutcome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventOutcomeProducerTest {

    @Mock
    private KafkaTemplate<String, EventOutcome> kafkaTemplate;

    private EventOutcomeProducer eventOutcomeProducer;

    @BeforeEach
    void setUp() {
        eventOutcomeProducer = new EventOutcomeProducer(kafkaTemplate, "event-outcomes");
    }

    @Test
    void shouldPublishEventOutcomeToKafka() {
        EventOutcome outcome = new EventOutcome(101L, "Football Match", 10L);

        eventOutcomeProducer.publish(outcome);

        verify(kafkaTemplate).send("event-outcomes", "101", outcome);
    }
}
