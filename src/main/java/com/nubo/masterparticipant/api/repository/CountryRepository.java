package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
