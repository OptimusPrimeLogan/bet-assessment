package com.sporty.settlement.controller;

import com.sporty.settlement.dto.response.ApiResponse;
import com.sporty.settlement.dto.response.BetResponse;
import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.exception.ResourceNotFoundException;
import com.sporty.settlement.service.SettlementService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetController {

    private final SettlementService settlementService;

    @GetMapping("/{eventId}")
    public ApiResponse<List<BetResponse>> getBetsByEvent(@PathVariable Long eventId) {
        List<BetResponse> bets = settlementService.getBetsByEventId(eventId).stream()
                .map(this::toResponse)
                .toList();
        if (bets.isEmpty()) {
            throw new ResourceNotFoundException("No bets found for eventId=" + eventId);
        }
        return ApiResponse.of(bets);
    }

    private BetResponse toResponse(Bet bet) {
        return new BetResponse(
                bet.getId(),
                bet.getUserId(),
                bet.getEventId(),
                bet.getMarketId(),
                bet.getWinnerId(),
                bet.getAmount(),
                bet.getStatus());
    }
}
