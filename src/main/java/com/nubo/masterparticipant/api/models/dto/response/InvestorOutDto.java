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
public class InvestorOutDto {

    private Long id;

    private String code;

    private String firstName;

    private String lastName;

    private String companyName;

    private TypeOutDto type;

    private StatusOutDto status;

    private CountryOutDto country;

    private DepartmentOutDto department;

    private CityOutDto city;

    private String documentNumber;

    private String documentType;

    private String nationality;

    private String address;

    private String phoneNumber;

    private String email;

    private String clearinghouseCode;

    private GicsOutDto gics;

    private String moneyLaunderingFlag;

    private TaxStatusOutDto taxStatus;

}
