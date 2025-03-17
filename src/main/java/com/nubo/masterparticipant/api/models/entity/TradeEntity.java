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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"TRADE\"", schema = "\"NUANBODB\"")
public class TradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"TRADE_ID\"", nullable = false)
    private Long id;

    @Column(name = "\"PARTICIPANT_ID_BUY\"", nullable = false)
    private Long participantIdBuy;

    @Column(name = "\"PARTICIPANT_ID_SELL\"", nullable = false)
    private Long participantIdSell;

}
