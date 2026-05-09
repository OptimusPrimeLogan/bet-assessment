package com.sporty.settlement.integration;

import com.sporty.settlement.dto.request.EventOutcomeRequest;
import com.sporty.settlement.entity.BetStatus;
import com.sporty.settlement.repository.BetRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SettlementIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private BetRepository betRepository;

    @Test
    void shouldSettleBetsAfterEventOutcomePublished() throws Exception {
        mockMvc.perform(post("/api/events/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EventOutcomeRequest(101L, "Football Match", 10L))))
                .andExpect(status().isAccepted());

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertThat(betRepository.findByEventIdAndStatus(101L, BetStatus.WON))
                    .isNotEmpty();
            assertThat(betRepository.findByEventIdAndStatus(101L, BetStatus.LOST))
                    .isNotEmpty();
            assertThat(betRepository.findByEventIdAndStatus(101L, BetStatus.PENDING))
                    .isEmpty();
        });
    }
}
