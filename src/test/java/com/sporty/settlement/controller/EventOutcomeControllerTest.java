package com.sporty.settlement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.settlement.dto.request.EventOutcomeRequest;
import com.sporty.settlement.kafka.EventOutcomePublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventOutcomeControllerTest {

    @Mock
    private EventOutcomePublisher eventOutcomePublisher;

    @InjectMocks
    private EventOutcomeController eventOutcomeController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventOutcomeController)
                .setControllerAdvice(new com.sporty.settlement.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void publishOutcome_shouldReturn202_whenRequestIsValid() throws Exception {
        doNothing().when(eventOutcomePublisher).publish(any());

        mockMvc.perform(post("/api/events/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EventOutcomeRequest(101L, "Football Match", 10L))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.eventId", is(101)))
                .andExpect(jsonPath("$.data.message", is("Event outcome published successfully")))
                .andExpect(jsonPath("$.system").exists());

        verify(eventOutcomePublisher).publish(any());
    }

    @Test
    void publishOutcome_shouldReturn400_whenEventIdIsNull() throws Exception {
        mockMvc.perform(post("/api/events/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EventOutcomeRequest(null, "Football Match", 10L))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publishOutcome_shouldReturn400_whenEventNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/events/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EventOutcomeRequest(101L, "", 10L))))
                .andExpect(status().isBadRequest());
    }
}
