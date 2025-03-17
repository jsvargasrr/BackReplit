package com.nubo.masterparticipant.api.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantAccountInDto {

    @NotNull
    @Min(value = 1)
    private Long participantId;

    @NotNull
    @Size(max = 50)
    private String accountNumber;

    @NotNull
    @Min(value = 1)
    private Long statusId;

    @NotNull
    @Min(value = 1)
    private Long typeId;

}
