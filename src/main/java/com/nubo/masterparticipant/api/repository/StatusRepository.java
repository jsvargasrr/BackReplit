package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
}
