package com.sporty.settlement.service;

import com.sporty.settlement.dto.BetSettlementStatus;
import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.entity.Bet;
import org.springframework.stereotype.Service;

/** Default settlement rule: bet wins if its winnerId matches the event outcome's winnerId. */
@Service
public class SettlementDecisionService implements BetSettlementDecision {

    @Override
    public BetSettlementStatus decide(Bet bet, EventOutcome outcome) {
        return bet.getWinnerId().equals(outcome.winnerId()) ? BetSettlementStatus.WON : BetSettlementStatus.LOST;
    }
}
