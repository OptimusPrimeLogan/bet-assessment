package com.sporty.settlement.service;

import com.sporty.settlement.dto.BetSettlementStatus;
import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.entity.Bet;

/** Strategy for deciding how a bet is settled. New market types implement this without touching SettlementService. */
public interface BetSettlementDecision {
    BetSettlementStatus decide(Bet bet, EventOutcome outcome);
}
