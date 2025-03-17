package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.response.IdentificationTypeOutDto;
import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.IdentificationTypeRepository;
import com.nubo.masterparticipant.api.service.IdentificationTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentificationTypeServiceImpl implements IdentificationTypeService {

    private final IdentificationTypeRepository identificationTypeRepository;

    @Override
    public List<IdentificationTypeOutDto> findAllIdentificationType() {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllIdentificationTypeV1();
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<IdentificationTypeOutDto> findAllIdentificationTypeV1() {
        log.info("Start method findAllIdentificationTypeV1");

        List<IdentificationTypeEntity> identificationTypeEntityList = identificationTypeRepository.findAll();

        List<IdentificationTypeOutDto> identificationTypeOutDtoList = identificationTypeEntityList.stream()
                .map(MapperUtil::identificationTypeEntityToIdentificationTypeOutDto)
                .toList();

        log.info("End method findAllIdentificationTypeV1, identificationTypeOutDtoList -> {}", identificationTypeOutDtoList);

        return identificationTypeOutDtoList;
    }

    @Override
    public List<IdentificationTypeOutDto> findIdentificationTypeByCountry(Long countryId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findIdentificationTypeByCountryV1(countryId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<IdentificationTypeOutDto> findIdentificationTypeByCountryV1(Long countryId) {
        log.info("Start method findIdentificationTypeByCountryV1");

        List<IdentificationTypeEntity> identificationTypeEntityList = identificationTypeRepository.findIdentificationTypeByCountry(countryId);

        if(identificationTypeEntityList.isEmpty()){
            throw new ServiceException(ErrorCode.IDENTIFICATION_TYPE_NOT_FOUND_FOR_THE_COUNTRY, HttpStatus.NOT_FOUND);
        }
        List<IdentificationTypeOutDto> identificationTypeOutDtoList = identificationTypeEntityList.stream()
                .map(MapperUtil::identificationTypeEntityToIdentificationTypeOutDto)
                .toList();

        log.info("End method findIdentificationTypeByCountryV1, identificationTypeOutDtoList -> {}", identificationTypeOutDtoList);

        return identificationTypeOutDtoList;
    }

}
