package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.request.ParticipantInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantOutDto;
import com.nubo.masterparticipant.api.service.ParticipantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantRestControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantRestController participantRestController;

    @Test
    @DisplayName("Find All Participant")
    void findAllParticipantTest() {
        Long status = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        when(participantService.findAllParticipant(pageable, status))
                .thenReturn(createPageResponseDtoTestData());

        ResponseEntity<PageResponseOutDto<ParticipantOutDto>> response = participantRestController
                .findAllParticipant(0, 10, status);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get Participant By Id")
    void findParticipantByIdTest() {
        when(participantService.findParticipantById(any())).thenReturn(createParticipantOutDtoTestData());

        ResponseEntity<ParticipantOutDto> participantOutDto = participantRestController.findParticipantById(1L);

        assertNotNull(participantOutDto.getBody());
        assertEquals(HttpStatus.OK, participantOutDto.getStatusCode());
    }

    @Test
    @DisplayName("Create Participant")
    void createParticipantTest() {
        when(participantService.createParticipant(any()))
                .thenReturn(createParticipantOutDtoTestData());

        ResponseEntity<ParticipantOutDto> participantOutDto = participantRestController
                .createParticipant(createParticipantInDtoTestData());

        assertNotNull(participantOutDto.getBody());
        assertEquals(HttpStatus.CREATED, participantOutDto.getStatusCode());
    }

    @Test
    @DisplayName("Update Participant")
    void updateParticipantTest() {
        when(participantService.updateParticipant(any(), any())).thenReturn(createParticipantOutDtoTestData());

        ResponseEntity<ParticipantOutDto> participantOutDto = participantRestController
                .updateParticipant(1L, createParticipantUpdateInDtoTestData());

        assertNotNull(participantOutDto.getBody());
        assertEquals(HttpStatus.OK, participantOutDto.getStatusCode());
    }

    @Test
    @DisplayName("Get Participant By Trade")
    void getParticipantByTradeTest() {

        Long tradeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        when(participantService.getParticipantByTrade(tradeId, pageable))
                .thenReturn(List.of(createParticipantOutDtoTestData()));

        ResponseEntity<List<ParticipantOutDto>> participantOutDtoList = participantRestController
                .getParticipantByTrade(tradeId, 0, 10);

        assertNotNull(participantOutDtoList.getBody());
        assertEquals(participantOutDtoList.getStatusCode().value(), HttpStatus.OK.value());
        assertEquals(1, participantOutDtoList.getBody().size());
    }

    private PageResponseOutDto<ParticipantOutDto> createPageResponseDtoTestData() {
        PageResponseOutDto<ParticipantOutDto> pageResponseDto = new PageResponseOutDto<>();
        pageResponseDto.setContent(List.of(createParticipantOutDtoTestData()));
        pageResponseDto.setPageSize(10);
        pageResponseDto.setPageNumber(0);
        pageResponseDto.setTotalPages(0);
        pageResponseDto.setTotalElements(1L);
        return pageResponseDto;
    }

    private ParticipantOutDto createParticipantOutDtoTestData() {
        return ParticipantOutDto.builder().build();
    }

    private ParticipantInDto createParticipantInDtoTestData() {
        return ParticipantInDto.builder().build();
    }

    private ParticipantUpdateInDto createParticipantUpdateInDtoTestData() {
        return ParticipantUpdateInDto.builder().build();
    }
}
