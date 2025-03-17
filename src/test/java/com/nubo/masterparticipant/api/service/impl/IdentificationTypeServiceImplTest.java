package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.response.IdentificationTypeOutDto;
import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.IdentificationTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdentificationTypeServiceImplTest {

    @Mock
    private IdentificationTypeRepository identificationTypeRepository;

    @InjectMocks
    private IdentificationTypeServiceImpl identificationTypeServiceImpl;

    private void setVersion(VersionApiEnum versionApiEnum) {
        GlobalContext.setProperty(PropertiesEnum.VERSION_API.getValue(), versionApiEnum.getValue());
    }

    @Test
    @DisplayName("Find all identification type - V1")
    void findAllIdentificationTypeV1Test() {
        this.setVersion(VersionApiEnum.V1);

        IdentificationTypeEntity identificationTypeEntity = createIdentificationTypeEntityTestData();
        when(identificationTypeRepository.findAll()).thenReturn(Collections.singletonList(identificationTypeEntity));

        List<IdentificationTypeOutDto> result = identificationTypeServiceImpl.findAllIdentificationType();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(identificationTypeEntity.getId(), result.get(0).getId());
        assertEquals(identificationTypeEntity.getDescription(), result.get(0).getDescription());
        verify(identificationTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find all identification type exception - Version Api Not Found")
    void findAllIdentificationTypeExceptionVersionNotImplementedTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                identificationTypeServiceImpl.findAllIdentificationType());

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Find identification type by country - V1")
    void findIdentificationTypeByCountryV1Test() {
        this.setVersion(VersionApiEnum.V1);

        Long countryId = 1L;
        IdentificationTypeEntity identificationTypeEntity = createIdentificationTypeEntityTestData();
        when(identificationTypeRepository.findIdentificationTypeByCountry(countryId)).thenReturn(Collections.singletonList(identificationTypeEntity));

        List<IdentificationTypeOutDto> result = identificationTypeServiceImpl.findIdentificationTypeByCountry(countryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(identificationTypeEntity.getId(), result.get(0).getId());
        assertEquals(identificationTypeEntity.getDescription(), result.get(0).getDescription());
        verify(identificationTypeRepository, times(1)).findIdentificationTypeByCountry(countryId);
    }

    @Test
    @DisplayName("Find identification type by country exception - Version Api Not Implemented")
    void findIdentificationTypeByCountryExceptionVersionNotImplementedTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                identificationTypeServiceImpl.findIdentificationTypeByCountry(1L));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Find identification type by country exception - Identification types not found for country")
    void findIdentificationTypeByCountryV1ExceptionIdentificationTypeNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        Long countryId = 1L;
        when(identificationTypeRepository.findIdentificationTypeByCountry(countryId)).thenReturn(Collections.emptyList());

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                identificationTypeServiceImpl.findIdentificationTypeByCountry(countryId));

        assertEquals(ErrorCode.IDENTIFICATION_TYPE_NOT_FOUND_FOR_THE_COUNTRY.name(), serviceException.getErrorCode().getName());
    }


    private IdentificationTypeEntity createIdentificationTypeEntityTestData() {
        String anyString = "any string";
        return IdentificationTypeEntity.builder()
                .id(1L)
                .description(anyString)
                .build();
    }

}
