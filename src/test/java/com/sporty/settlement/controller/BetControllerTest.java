package com.sporty.settlement.controller;

import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.entity.BetStatus;
import com.sporty.settlement.service.SettlementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

    @Mock
    private SettlementService settlementService;

    @InjectMocks
    private BetController betController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(betController)
                .setControllerAdvice(new com.sporty.settlement.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void getBetsByEvent_shouldReturnBets_whenFound() throws Exception {
        when(settlementService.getBetsByEventId(101L)).thenReturn(List.of(buildBet(1L, BetStatus.WON)));

        mockMvc.perform(get("/api/bets/event/101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].status", is("WON")))
                .andExpect(jsonPath("$.system").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getBetsByEvent_shouldReturn404_whenNoBetsFound() throws Exception {
        when(settlementService.getBetsByEventId(999L)).thenReturn(List.of());

        mockMvc.perform(get("/api/bets/event/999")).andExpect(status().isNotFound());
    }

    private Bet buildBet(Long id, BetStatus status) {
        Bet bet = new Bet();
        bet.setId(id);
        bet.setUserId(1L);
        bet.setEventId(101L);
        bet.setMarketId(1L);
        bet.setWinnerId(10L);
        bet.setAmount(BigDecimal.valueOf(50));
        bet.setStatus(status);
        return bet;
    }
}
