package com.sporty.settlement.rocketmq;

import com.sporty.settlement.dto.BetSettlement;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BetSettlementProducer implements BetSettlementDispatcher {

    private final String settlementTopic;
    private final BetSettlementHandler betSettlementHandler;

    public BetSettlementProducer(
            BetSettlementHandler betSettlementHandler,
            @Value("${app.kafka.topics.bet-settlements:bet-settlements}") String settlementTopic) {
        this.betSettlementHandler = betSettlementHandler;
        this.settlementTopic = settlementTopic;
    }

    @Override
    public void send(BetSettlement settlement) {
        log.info("[RocketMQ MOCK] Sending to {}: {}", settlementTopic, settlement);
        betSettlementHandler.onMessage(settlement);
    }
}
