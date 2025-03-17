package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
}
