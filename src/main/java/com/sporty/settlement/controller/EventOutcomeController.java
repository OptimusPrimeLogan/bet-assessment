package com.sporty.settlement.controller;

import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.dto.request.EventOutcomeRequest;
import com.sporty.settlement.dto.response.ApiResponse;
import com.sporty.settlement.dto.response.EventOutcomeResponse;
import com.sporty.settlement.kafka.EventOutcomePublisher;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventOutcomeController {

    private final EventOutcomePublisher eventOutcomePublisher;

    @PostMapping("/outcome")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse<EventOutcomeResponse> publishOutcome(@Valid @RequestBody EventOutcomeRequest request) {
        eventOutcomePublisher.publish(new EventOutcome(request.eventId(), request.eventName(), request.winnerId()));
        return ApiResponse.of(new EventOutcomeResponse(
                request.eventId(), request.eventName(), request.winnerId(), "Event outcome published successfully"));
    }
}
