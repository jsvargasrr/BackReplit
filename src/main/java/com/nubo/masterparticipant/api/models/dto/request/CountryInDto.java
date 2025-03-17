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
public class CountryInDto {

    @Size(max = 3)
    private String code;

    @Size(max = 30)
    private String name;

    @Min(value = 1)
    private Long currencyId;

    @Size(max = 30)
    private String timeZone;
}
