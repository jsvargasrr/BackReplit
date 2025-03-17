package com.nubo.masterparticipant.api.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@IdClass(ParticipantInvestorKey.class)
@Table(name = "\"PARTICIPANT_INVESTOR\"", schema = "\"NUANBODB\"")
public class ParticipantInvestorEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "\"PARTICIPANT_ID\"", nullable = false)
    private ParticipantEntity participant;

    @Id
    @ManyToOne
    @JoinColumn(name = "\"INVESTOR_ID\"", nullable = false)
    private InvestorEntity investor;

    @Column(name = "\"LAST_MODIFY_USER\"", length = 250, nullable = false)
    private String lastModifyUser;

    @Column(name = "\"LAST_MODIFY_APPLIC\"", length = 250, nullable = false)
    private String lastModifyApplication;

    @Column(name = "\"LAST_MODIFY_DATE\"", nullable = false)
    private LocalDateTime lastModifyDate;

    @Column(name = "\"LAST_MODIFY_IP\"", length = 50, nullable = false)
    private String lastModifyIp;

}
