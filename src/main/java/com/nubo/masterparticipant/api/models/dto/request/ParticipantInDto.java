package com.nubo.masterparticipant.api.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantInDto {

    @NotNull
    @Size(max = 6)
    private String code;

    @NotNull
    @Size(max = 20)
    private String documentNumber;

    @NotNull
    @Min(value = 1)
    private Long identificationTypeId;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String shortName;

    @NotNull
    @Min(value = 1)
    private Long typeId;

    @Size(max = 4)
    private String mnemonic;

    @NotNull
    @Min(value = 1)
    private Long countryId;

    private LocalDate registrationDate;

    @Size(max = 20)
    private String taxCode;

    @NotNull
    @Size(max = 10)
    private String departmentId;

    @Size(max = 100)
    private String mainAddress;

    @Size(max = 100)
    private String webPage;

    @Size(max = 20)
    private String phoneNumber;

    @NotNull
    @Min(value = 1)
    private Long statusId;

    @Size(max = 20)
    private String nitClearingHouse;

    @NotNull
    @Min(value = 1)
    private Long taxStatusId;

    @Size(max = 1)
    private String typeEntity;

    @Size(max = 1)
    private String marginAccount;

    @Size(max = 1)
    private String masterAccount;

    @Size(max = 1)
    private String directAllocation;

    @Size(max = 1)
    private String marketMaker;

    private Boolean enabled;

    private Boolean custodian;

}
