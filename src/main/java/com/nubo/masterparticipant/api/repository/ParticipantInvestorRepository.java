package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.ParticipantInvestorEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantInvestorKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParticipantInvestorRepository extends JpaRepository<ParticipantInvestorEntity, ParticipantInvestorKey>,
        JpaSpecificationExecutor<ParticipantInvestorEntity> {
}
