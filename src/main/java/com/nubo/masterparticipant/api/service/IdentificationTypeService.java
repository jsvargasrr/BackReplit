package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.response.IdentificationTypeOutDto;

import java.util.List;

public interface IdentificationTypeService {

    List<IdentificationTypeOutDto> findAllIdentificationType();

    List<IdentificationTypeOutDto> findIdentificationTypeByCountry(Long countryId);

}
