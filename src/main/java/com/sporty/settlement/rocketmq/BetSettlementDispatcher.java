package com.sporty.settlement.rocketmq;

import com.sporty.settlement.dto.BetSettlement;

public interface BetSettlementDispatcher {
    void send(BetSettlement settlement);
}

