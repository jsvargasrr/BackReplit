package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.response.DepositoryOutDto;
import com.nubo.masterparticipant.api.service.DepositoryService;
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
class DepositoryRestControllerTest {

    @Mock
    private DepositoryService depositoryService;

    @InjectMocks
    private DepositoryRestController depositoryRestController;

    @Test
    @DisplayName("Find all depository")
    void findAllDepositoryTest() {

        List<DepositoryOutDto> depositoryOutDtoList = List.of();
        when(depositoryService.findAllDepository()).thenReturn(depositoryOutDtoList);

        ResponseEntity<List<DepositoryOutDto>> response = depositoryRestController.findAllDepository();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(depositoryOutDtoList, response.getBody());
        verify(depositoryService).findAllDepository();
    }

}
