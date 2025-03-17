package com.nubo.masterparticipant.api.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"DEPARTMENT\"", schema = "\"NUANBODB\"")
public class DepartmentEntity {

    @Id
    @Column(name = "\"DEPARTMENT_ID\"", length = 10, nullable = false)
    private String id;

    @Column(name = "\"DESCRIPTION\"", length = 80)
    private String description;

    @Column(name = "\"LAST_MODIFY_USER\"", length = 250, nullable = false)
    private String lastModifyUser;

    @Column(name = "\"LAST_MODIFY_APPLIC\"", length = 250, nullable = false)
    private String lastModifyApplication;

    @Column(name = "\"LAST_MODIFY_DATE\"", nullable = false)
    private LocalDateTime lastModifyDate;

    @Column(name = "\"LAST_MODIFY_IP\"", length = 50, nullable = false)
    private String lastModifyIp;

}