package com.nubo.masterparticipant.api.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantInvestorInDto {

    @NotNull
    @Min(value = 1)
    private Long participantId;

    @NotNull
    @Min(value = 1)
    private Long investorId;

}
