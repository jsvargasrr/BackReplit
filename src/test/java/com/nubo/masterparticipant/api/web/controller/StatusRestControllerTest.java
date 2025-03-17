package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.response.StatusOutDto;
import com.nubo.masterparticipant.api.service.StatusService;
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
class StatusRestControllerTest {

    @Mock
    private StatusService statusService;

    @InjectMocks
    private StatusRestController statusRestController;

    @Test
    @DisplayName("Find all status")
    void findAllStatusTest() {

        List<StatusOutDto> statusOutDtoList = List.of();
        when(statusService.findAllStatus()).thenReturn(statusOutDtoList);

        ResponseEntity<List<StatusOutDto>> response = statusRestController.findAllStatus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(statusOutDtoList, response.getBody());
        verify(statusService).findAllStatus();
    }

}
