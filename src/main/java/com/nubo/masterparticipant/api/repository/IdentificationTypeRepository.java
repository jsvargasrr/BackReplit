package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IdentificationTypeRepository extends JpaRepository<IdentificationTypeEntity, Long> {

    @Query("SELECT i FROM IdentificationTypeEntity i " +
            "WHERE i.countryId = :countryId")
    List<IdentificationTypeEntity> findIdentificationTypeByCountry(Long countryId);

}
