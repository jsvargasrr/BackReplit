package com.nubo.masterparticipant.api.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ParticipantAccountOutDto {

    private Long id;

    private ParticipantOutDto participant;

    private String accountNumber;

    private StatusOutDto status;

    private TypeOutDto type;

}
