package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.InvestorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<InvestorEntity, Long> {
}
