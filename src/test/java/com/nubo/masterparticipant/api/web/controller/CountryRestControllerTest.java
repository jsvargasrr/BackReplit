package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.request.CountryInDto;
import com.nubo.masterparticipant.api.models.dto.response.CountryOutDto;
import com.nubo.masterparticipant.api.service.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryRestControllerTest {

    private static final String COUNTRY_COLOMBIA = "Colombia";
    private static final String COUNTRY_PERU = "Peru";

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryRestController countryRestController;

    @Test
    @DisplayName("Find all countries")
    void findAllCountriesTest() {
        List<CountryOutDto> countryList = List.of(
                createCountryDtoTestData(1L, COUNTRY_COLOMBIA),
                createCountryDtoTestData(2L, COUNTRY_PERU)
        );
        when(countryService.findAllCountries()).thenReturn(countryList);

        ResponseEntity<List<CountryOutDto>> response = countryRestController.findAllCountries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryList, response.getBody());
        verify(countryService).findAllCountries();
    }

    @Test
    @DisplayName("Find country by id")
    void findCountryByIdTest() {
        Long countryId = 1L;
        CountryOutDto countryOutDto = createCountryDtoTestData(countryId, COUNTRY_COLOMBIA);
        when(countryService.findCountryById(countryId)).thenReturn(countryOutDto);

        ResponseEntity<CountryOutDto> response = countryRestController.findCountryById(countryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryOutDto, response.getBody());
        verify(countryService).findCountryById(countryId);
    }

    @Test
    @DisplayName("Update Country")
    void updateCountryTest() {

        CountryOutDto countryOutDto = createCountryDtoTestData(1L, COUNTRY_COLOMBIA);
        when(countryService.updateCountry(anyLong(), any(CountryInDto.class)))
                .thenReturn(countryOutDto);

        ResponseEntity<CountryOutDto> response = countryRestController.updateCountry(1L, createCountryInDtoTestData());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryOutDto, response.getBody());
    }

    private CountryOutDto createCountryDtoTestData(Long id, String name) {
        return CountryOutDto.builder()
                .id(id)
                .name(name)
                .build();
    }

    private CountryInDto createCountryInDtoTestData() {
        return CountryInDto.builder().build();
    }

}
