package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<TradeEntity, Long> {
}
