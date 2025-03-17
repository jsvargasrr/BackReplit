package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.ParticipantAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantAccountRepository extends JpaRepository<ParticipantAccountEntity, Long>,
        JpaSpecificationExecutor<ParticipantAccountEntity> {

    @Query("SELECT DISTINCT pa FROM SettlementEntity s " +
            "JOIN ParticipantAccountEntity pa ON s.participantAccountId = pa.id " +
            "WHERE s.tradeId = :tradeId")
    Page<ParticipantAccountEntity> findParticipantAccountsByTradeId(@Param("tradeId") Long tradeId, Pageable pageable);


}
