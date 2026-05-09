package com.sporty.settlement.rocketmq;

import com.sporty.settlement.dto.BetSettlement;

public interface BetSettlementHandler {
    void onMessage(BetSettlement settlement);
}