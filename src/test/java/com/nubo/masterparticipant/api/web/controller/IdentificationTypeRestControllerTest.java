package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.response.IdentificationTypeOutDto;
import com.nubo.masterparticipant.api.service.IdentificationTypeService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdentificationTypeRestControllerTest {

    @Mock
    private IdentificationTypeService identificationTypeService;

    @InjectMocks
    private IdentificationTypeRestController identificationTypeRestController;

    @Test
    @DisplayName("Find all identification type")
    void findAllIdentificationTypeTest() {

        List<IdentificationTypeOutDto> identificationTypeOutDtoList = List.of();
        when(identificationTypeService.findAllIdentificationType()).thenReturn(identificationTypeOutDtoList);

        ResponseEntity<List<IdentificationTypeOutDto>> response = identificationTypeRestController.findAllIdentificationType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(identificationTypeOutDtoList, response.getBody());
        verify(identificationTypeService).findAllIdentificationType();
    }

    @Test
    @DisplayName("Find identification type by country")
    void findIdentificationTypeByCountryTest() {

        Long countryId = 1L;
        List<IdentificationTypeOutDto> identificationTypeOutDtoList = List.of();
        when(identificationTypeService.findIdentificationTypeByCountry(countryId)).thenReturn(identificationTypeOutDtoList);

        ResponseEntity<List<IdentificationTypeOutDto>> response = identificationTypeRestController.findIdentificationTypeByCountry(countryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(identificationTypeOutDtoList, response.getBody());
        verify(identificationTypeService).findIdentificationTypeByCountry(countryId);
    }

}
