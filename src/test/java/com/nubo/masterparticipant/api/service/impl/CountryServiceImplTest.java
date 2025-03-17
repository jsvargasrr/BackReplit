package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.util.ApplicationUtils;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.request.CountryInDto;
import com.nubo.masterparticipant.api.models.dto.response.CountryOutDto;
import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import com.nubo.masterparticipant.api.models.entity.CurrencyEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.CountryRepository;
import com.nubo.masterparticipant.api.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private PropertiesConfig propertiesConfig;

    @InjectMocks
    private CountryServiceImpl countryServiceImpl;

    @BeforeEach
    void setup() {
        GlobalContext.setProperty(PropertiesEnum.CLIENT_IP.getValue(), "any string");
        GlobalContext.setProperty(PropertiesEnum.CLIENT_USER.getValue(), "any string");
        GlobalContext.setProperty(PropertiesEnum.CLIENT_DATETIME.getValue(), ApplicationUtils.getLocalDateTimeNow());
    }

    private void setVersion(VersionApiEnum versionApiEnum) {
        GlobalContext.setProperty(PropertiesEnum.VERSION_API.getValue(), versionApiEnum.getValue());
    }

    @Test
    @DisplayName("Find all countries - V1")
    void findAllCountriesV1Test() {
        this.setVersion(VersionApiEnum.V1);

        CountryEntity countryEntity = createCountryEntityTestData();
        when(countryRepository.findAll()).thenReturn(Collections.singletonList(countryEntity));

        List<CountryOutDto> result = countryServiceImpl.findAllCountries();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(countryEntity.getId(), result.get(0).getId());
        assertEquals(countryEntity.getName(), result.get(0).getName());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find all countries exception - Version Api Not Found")
    void findAllCountriesExceptionVersionNotImplementedTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                countryServiceImpl.findAllCountries());

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Find country by id - V1")
    void findCountryByIdV1Test() {
        this.setVersion(VersionApiEnum.V1);

        CountryEntity countryEntity = createCountryEntityTestData();
        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(countryEntity));

        CountryOutDto result = countryServiceImpl.findCountryById(1L);

        assertNotNull(result);
        assertEquals(countryEntity.getId(), result.getId());
        assertEquals(countryEntity.getName(), result.getName());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find country by id - Country not found - V1")
    void findCountryByIdExceptionCountryNotFoundV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(countryRepository.findById(anyLong())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> countryServiceImpl.findCountryById(1L));
        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
        assertEquals(404, exception.getHttpStatus().value());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find country by id exception - Version Api Not Found")
    void findCountryByIdExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                countryServiceImpl.findCountryById(1L));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update Country - V1")
    void updateCountryV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(createCountryEntityTestData()));
        when(currencyRepository.findById(anyLong())).thenReturn(Optional.of(createCurrencyEntityTestData()));
        when(countryRepository.save(any())).thenReturn(createCountryEntityTestData());
        when(propertiesConfig.getApiName()).thenReturn("any string");

        CountryOutDto countryOutDto = countryServiceImpl.updateCountry(1L, createCountryInDtoTestData());

        assertNotNull(countryOutDto);
    }

    @Test
    @DisplayName("Update Country - exception - Version Api Not Found")
    void updateCountryExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        CountryInDto countryInDto = createCountryInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                countryServiceImpl.updateCountry(1L, countryInDto));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update Country - Country not found - V1")
    void updateCountryV1ExceptionCountryNotFoundV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(countryRepository.findById(anyLong())).thenReturn(Optional.empty());

        CountryInDto countryInDto = createCountryInDtoTestData();
        ServiceException exception = assertThrows(ServiceException.class,
                () -> countryServiceImpl.updateCountry(1L, countryInDto));

        assertEquals(ErrorCode.COUNTRY_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Update Country - Currency not found - V1")
    void updateCountryV1ExceptionCurrencyNotFoundV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(createCountryEntityTestData()));
        when(currencyRepository.findById(anyLong())).thenReturn(Optional.empty());

        CountryInDto countryInDto = createCountryInDtoTestData();
        ServiceException exception = assertThrows(ServiceException.class,
                () -> countryServiceImpl.updateCountry(1L, countryInDto));

        assertEquals(ErrorCode.CURRENCY_NOT_FOUND, exception.getErrorCode());
    }

    private CountryEntity createCountryEntityTestData() {
        String anyString = "any string";
        return CountryEntity.builder()
                .id(1L)
                .name(anyString)
                .currency(createCurrencyEntityTestData())
                .timeZone(anyString)
                .build();
    }

    private CurrencyEntity createCurrencyEntityTestData() {
        String anyString = "any string";
        return CurrencyEntity.builder()
                .id(1L)
                .code(anyString)
                .name(anyString)
                .build();
    }

    private CountryInDto createCountryInDtoTestData() {
        String anyString = "any string";
        return CountryInDto.builder()
                .name(anyString)
                .currencyId(1L)
                .timeZone(anyString)
                .build();
    }

}
