package com.sporty.settlement.service;

import com.sporty.settlement.dto.BetSettlement;
import com.sporty.settlement.dto.EventOutcome;
import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.entity.BetStatus;
import com.sporty.settlement.repository.BetRepository;
import com.sporty.settlement.rocketmq.BetSettlementDispatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final BetRepository betRepository;
    private final BetSettlementDispatcher betSettlementDispatcher;
    private final BetSettlementDecision betSettlementDecision;

    /**
     * A second delivery of the same event finds no PENDING bets (already WON/LOST), so no double-settlement can occur.
     */
    @Transactional
    public void processEventOutcome(EventOutcome outcome) {
        List<Bet> pendingBets = betRepository.findByEventIdAndStatus(outcome.eventId(), BetStatus.PENDING);
        if (pendingBets.isEmpty()) {
            log.info("No pending bets for eventId={}", outcome.eventId());
            return;
        }

        log.info("Settling {} bets for eventId={}", pendingBets.size(), outcome.eventId());
        for (Bet bet : pendingBets) {
            var status = betSettlementDecision.decide(bet, outcome);
            betSettlementDispatcher.send(new BetSettlement(
                    bet.getId(), bet.getUserId(), bet.getEventId(), bet.getWinnerId(), bet.getAmount(), status));
        }
    }

    public List<Bet> getBetsByEventId(Long eventId) {
        return betRepository.findByEventId(eventId);
    }
}
