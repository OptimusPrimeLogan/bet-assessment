package com.sporty.settlement.rocketmq;

import com.sporty.settlement.dto.BetSettlement;
import com.sporty.settlement.entity.BetStatus;
import com.sporty.settlement.repository.BetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BetSettlementConsumer implements BetSettlementHandler {

    private final BetRepository betRepository;

    @Override
    @Transactional
    public void onMessage(BetSettlement settlement) {
        log.info("[RocketMQ MOCK] Consuming from bet-settlements: {}", settlement);
        betRepository
                .findById(settlement.betId())
                .ifPresentOrElse(
                        bet -> {
                            bet.setStatus(BetStatus.valueOf(settlement.status().name()));
                            betRepository.save(bet);
                            log.info("Bet {} settled as {}", bet.getId(), bet.getStatus());
                        },
                        () -> log.warn("Bet not found: {}", settlement.betId()));
    }
}