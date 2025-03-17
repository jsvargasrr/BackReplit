package com.nubo.masterparticipant.api.models.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ParticipantOutDto {

    private Long id;

    private String code;

    private String documentNumber;

    private IdentificationTypeOutDto identificationType;

    private String name;

    private String shortName;

    private TypeOutDto type;

    private String mnemonic;

    private CountryOutDto country;

    private LocalDate registrationDate;

    private String taxCode;

    private DepartmentOutDto department;

    private String mainAddress;

    private String webPage;

    private String phoneNumber;

    private StatusOutDto status;

    private String nitClearingHouse;

    private TaxStatusOutDto taxStatus;

    private String typeEntity;

    private String marginAccount;

    private String masterAccount;

    private String directAllocation;

    private String marketMaker;

    private Boolean enabled;

    private Boolean custodian;

}
