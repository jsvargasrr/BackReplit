package com.nubo.masterparticipant.api.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "\"LOG_EVENT\"", schema = "\"NUANBODB\"")
public class LogEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"LOG_EVENT_ID\"", nullable = false)
    private Long id;

    @Column(name = "\"NUMBER\"")
    private Integer number;

    @Column(name = "\"DESCRIPTION\"", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "\"UUID\"", length = 36)
    private String uuid;

    @Column(name = "\"TRACKING\"", nullable = false)
    private LocalDateTime tracking;

    @Column(name = "\"ORIGIN\"", columnDefinition = "TEXT")
    private String origin;

    @Column(name = "\"LAST_MODIFY_USER\"", length = 250, nullable = false)
    private String lastModifyUser;

    @Column(name = "\"LAST_MODIFY_APPLIC\"", length = 250, nullable = false)
    private String lastModifyApplication;

    @Column(name = "\"LAST_MODIFY_DATE\"", nullable = false)
    private LocalDateTime lastModifyDate;

    @Column(name = "\"LAST_MODIFY_IP\"", length = 50, nullable = false)
    private String lastModifyIp;

}


