package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;
import com.nubo.masterparticipant.api.service.TypeService;
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
class TypeRestControllerTest {

    @Mock
    private TypeService typeService;

    @InjectMocks
    private TypeRestController typeRestController;

    @Test
    @DisplayName("Find all type")
    void findAllTypeTest() {

        List<TypeOutDto> typeOutDtoList = List.of();
        when(typeService.findAllType()).thenReturn(typeOutDtoList);

        ResponseEntity<List<TypeOutDto>> response = typeRestController.findAllType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeOutDtoList, response.getBody());
        verify(typeService).findAllType();
    }

}
