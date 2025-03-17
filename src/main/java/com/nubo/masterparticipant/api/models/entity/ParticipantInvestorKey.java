package com.nubo.masterparticipant.api.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantInvestorKey implements Serializable {

    private Long participant;

    private Long investor;

}
