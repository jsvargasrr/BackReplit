package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantAccountOutDto;
import com.nubo.masterparticipant.api.models.dto.response.StatusOutDto;
import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;
import com.nubo.masterparticipant.api.service.ParticipantAccountService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantAccountRestControllerTest {

    @Mock
    private ParticipantAccountService participantAccountService;

    @InjectMocks
    private ParticipantAccountRestController participantAccountRestController;

    @Test
    @DisplayName("Find All ParticipantAccount")
    void findAllParticipantAccountsTest() {
        Long status = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(participantAccountService.findAllParticipantAccount(pageable, status))
                .thenReturn(createPageResponseDtoTestData());

        ResponseEntity<PageResponseOutDto<ParticipantAccountOutDto>> response = participantAccountRestController
                .findAllParticipantAccounts(0, 10, status);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Find ParticipantAccount by Id")
    void findParticipantAccountByIdTest() {
        when(participantAccountService.findParticipantAccountById(anyLong()))
                .thenReturn(createParticipantAccountOutDtoTestData());

        ResponseEntity<ParticipantAccountOutDto> response = participantAccountRestController
                .findParticipantAccountById(1L);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Create ParticipantAccount")
    void createParticipantAccountTest() {
        when(participantAccountService.createParticipantAccount(any()))
                .thenReturn(createParticipantAccountOutDtoTestData());

        ResponseEntity<ParticipantAccountOutDto> response = participantAccountRestController
                .createParticipantAccount(createParticipantAccountInDtoTestData());

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Update ParticipantAccount")
    void updateParticipantAccountTest() {
        when(participantAccountService.updateParticipantAccount(anyLong(), any()))
                .thenReturn(createParticipantAccountOutDtoTestData());

        ResponseEntity<ParticipantAccountOutDto> response = participantAccountRestController
                .updateParticipantAccount(1L, createParticipantAccountUpdateInDtoTestData());

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Get Participant Accounts by Trade - Success")
    void getParticipantAccountsByTradeSuccessTest() {
        Long tradeId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        List<ParticipantAccountOutDto> participantAccounts = List.of(createParticipantAccountOutDtoTestData());

        when(participantAccountService.getParticipantAccountsByTrade(tradeId, pageable))
                .thenReturn(participantAccounts);

        ResponseEntity<List<ParticipantAccountOutDto>> response =
                participantAccountRestController.getParticipantAccountsByTrade(tradeId, page, size);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(participantAccounts.get(0).getId(), response.getBody().get(0).getId());
    }

    @Test
    @DisplayName("Get Participant Accounts by Trade - Empty Result")
    void getParticipantAccountsByTradeEmptyResultTest() {
        Long tradeId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(participantAccountService.getParticipantAccountsByTrade(tradeId, pageable))
                .thenReturn(List.of());

        ResponseEntity<List<ParticipantAccountOutDto>> response =
                participantAccountRestController.getParticipantAccountsByTrade(tradeId, page, size);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    @DisplayName("Get Participant Accounts by Trade - Invalid Trade ID")
    void getParticipantAccountsByTradeInvalidTradeIdTest() {
        Long tradeId = 999L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(participantAccountService.getParticipantAccountsByTrade(tradeId, pageable))
                .thenThrow(new ServiceException(ErrorCode.PARTICIPANT_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));

        ServiceException exception = assertThrows(ServiceException.class, () ->
                participantAccountRestController.getParticipantAccountsByTrade(tradeId, page, size));

        assertEquals(ErrorCode.PARTICIPANT_ACCOUNT_NOT_FOUND, exception.getErrorCode());
    }

    private PageResponseOutDto<ParticipantAccountOutDto> createPageResponseDtoTestData() {
        PageResponseOutDto<ParticipantAccountOutDto> pageResponseDto = new PageResponseOutDto<>();
        pageResponseDto.setContent(List.of(createParticipantAccountOutDtoTestData()));
        pageResponseDto.setPageSize(10);
        pageResponseDto.setPageNumber(0);
        pageResponseDto.setTotalPages(0);
        pageResponseDto.setTotalElements(1L);
        return pageResponseDto;
    }

    private ParticipantAccountOutDto createParticipantAccountOutDtoTestData() {
        return ParticipantAccountOutDto.builder()
                .id(1L)
                .accountNumber("123456789")
                .status(StatusOutDto.builder().build())
                .type(TypeOutDto.builder().build())
                .build();
    }

    private ParticipantAccountInDto createParticipantAccountInDtoTestData() {
        return ParticipantAccountInDto.builder().build();
    }

    private ParticipantAccountUpdateInDto createParticipantAccountUpdateInDtoTestData() {
        return ParticipantAccountUpdateInDto.builder().build();
    }
}
