package com.nubo.masterparticipant.api.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantAccountUpdateInDto {

    @Min(value = 1)
    private Long participantId;

    @Size(max = 50)
    private String accountNumber;

    @Min(value = 1)
    private Long statusId;

    @Min(value = 1)
    private Long typeId;

}
