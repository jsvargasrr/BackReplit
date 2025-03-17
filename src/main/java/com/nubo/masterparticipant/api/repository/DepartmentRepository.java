package com.nubo.masterparticipant.api.repository;

import com.nubo.masterparticipant.api.models.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {
}
