package com.sporty.settlement.repository;

import com.sporty.settlement.entity.Bet;
import com.sporty.settlement.entity.BetStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByEventIdAndStatus(Long eventId, BetStatus status);

    List<Bet> findByEventId(Long eventId);
}
