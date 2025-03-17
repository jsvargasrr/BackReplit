package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.TaxStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxStatusRepository extends JpaRepository<TaxStatusEntity, Long> {
}
