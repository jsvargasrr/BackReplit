package com.nubo.masterparticipant.api.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"SETTLEMENT\"", schema = "\"NUANBODB\"")
public class SettlementEntity {

    @Id
    @Column(name = "\"TRADE_ID\"", nullable = false)
    private Integer tradeId;

    @Column(name = "\"PARTICIPANT_ACCOUNT_ID\"", nullable = false)
    private Long participantAccountId;

}