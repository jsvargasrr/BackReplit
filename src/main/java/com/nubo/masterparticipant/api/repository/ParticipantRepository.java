package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long>,
        JpaSpecificationExecutor<ParticipantEntity> {

    @Query("SELECT p FROM ParticipantEntity p WHERE p.id IN " +
            "(SELECT t.participantIdBuy FROM TradeEntity t WHERE t.id = :tradeId) " +
            "OR p.id IN (SELECT t.participantIdSell FROM TradeEntity t WHERE t.id = :tradeId)")
    Page<ParticipantEntity> findParticipantsByTradeId(@Param("tradeId") Long tradeId, Pageable pageable);

}
