package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
}
