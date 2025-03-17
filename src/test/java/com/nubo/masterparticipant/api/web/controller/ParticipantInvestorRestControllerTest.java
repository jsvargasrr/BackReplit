package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.models.dto.request.ParticipantInvestorInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantInvestorOutDto;
import com.nubo.masterparticipant.api.service.ParticipantInvestorService;
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
class ParticipantInvestorRestControllerTest {

    @Mock
    private ParticipantInvestorService participantInvestorService;

    @InjectMocks
    private ParticipantInvestorRestController participantInvestorRestController;

    @Test
    @DisplayName("Find All ParticipantInvestor")
    void findParticipantsByInvestorIdTest() {
        Long participantId = 1L;
        Long investorId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(participantInvestorService.findAllParticipantInvestor(pageable, participantId, investorId))
                .thenReturn(createPageResponseDtoTestData());

        ResponseEntity<PageResponseOutDto<ParticipantInvestorOutDto>> response = participantInvestorRestController
                .findAllParticipantInvestor(0, 10, 1L, 1L);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Create ParticipantInvestor")
    void createParticipantInvestorTest() {
        when(participantInvestorService.createParticipantInvestor(any(ParticipantInvestorInDto.class)))
                .thenReturn(createParticipantInvestorOutDtoTestData());

        ResponseEntity<ParticipantInvestorOutDto> response = participantInvestorRestController
                .createParticipantInvestor(createParticipantInvestorInDtoTestData());

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private PageResponseOutDto<ParticipantInvestorOutDto> createPageResponseDtoTestData() {
        PageResponseOutDto<ParticipantInvestorOutDto> pageResponseDto = new PageResponseOutDto<>();
        pageResponseDto.setContent(List.of(createParticipantInvestorOutDtoTestData()));
        pageResponseDto.setPageSize(10);
        pageResponseDto.setPageNumber(0);
        pageResponseDto.setTotalPages(0);
        pageResponseDto.setTotalElements(1L);
        return pageResponseDto;
    }

    private ParticipantInvestorOutDto createParticipantInvestorOutDtoTestData() {
        return ParticipantInvestorOutDto.builder().build();
    }

    private ParticipantInvestorInDto createParticipantInvestorInDtoTestData() {
        return ParticipantInvestorInDto.builder().build();
    }
}
