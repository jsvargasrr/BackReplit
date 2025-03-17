package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.LogEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEventRepository extends JpaRepository<LogEventEntity, Long> {
}
