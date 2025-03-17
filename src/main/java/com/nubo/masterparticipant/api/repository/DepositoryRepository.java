package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.DepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositoryRepository extends JpaRepository<DepositoryEntity, String> {
}
