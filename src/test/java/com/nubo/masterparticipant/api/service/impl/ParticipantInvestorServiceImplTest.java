package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.util.ApplicationUtils;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantInvestorInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantInvestorOutDto;
import com.nubo.masterparticipant.api.models.entity.InvestorEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantInvestorEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.InvestorRepository;
import com.nubo.masterparticipant.api.repository.ParticipantInvestorRepository;
import com.nubo.masterparticipant.api.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantInvestorServiceImplTest {

    @Mock
    private ParticipantInvestorRepository participantInvestorRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private PropertiesConfig propertiesConfig;

    @InjectMocks
    private ParticipantInvestorServiceImpl participantInvestorServiceImpl;

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
    @DisplayName("Find All ParticipantInvestor - V1")
    void findAllParticipantInvestorV1Test() {
        this.setVersion(VersionApiEnum.V1);

        Pageable pageable = PageRequest.of(0, 10);
        List<ParticipantInvestorEntity> tradeEntities = List.of(createParticipantInvestorEntityTestData());
        Page<ParticipantInvestorEntity> page = new PageImpl<>(tradeEntities, pageable, 1);

        when(participantInvestorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        PageResponseOutDto<ParticipantInvestorOutDto> participantInvestorOutDtoPage = participantInvestorServiceImpl
                .findAllParticipantInvestor(pageable, null, null);

        assertNotNull(participantInvestorOutDtoPage);
        assertEquals(1, participantInvestorOutDtoPage.getTotalElements());
    }

    @Test
    @DisplayName("Find All ParticipantInvestor with ParticipantId and InvestorId - V1")
    void findAllParticipantInvestorV1FilterTest() {
        this.setVersion(VersionApiEnum.V1);

        Long participantId = 1L;
        Long investorId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<ParticipantInvestorEntity> tradeEntities = List.of(createParticipantInvestorEntityTestData());
        Page<ParticipantInvestorEntity> page = new PageImpl<>(tradeEntities, pageable, 1);

        when(participantInvestorRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        PageResponseOutDto<ParticipantInvestorOutDto> participantInvestorOutDtoPage = participantInvestorServiceImpl
                .findAllParticipantInvestor(pageable, participantId, investorId);

        assertNotNull(participantInvestorOutDtoPage);
        assertEquals(1, participantInvestorOutDtoPage.getTotalElements());
    }

    @Test
    @DisplayName("Find All ParticipantInvestor exception - Version Api Not Found")
    void findAllParticipantInvestorExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        Pageable pageable = PageRequest.of(0, 10);
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantInvestorServiceImpl.findAllParticipantInvestor(pageable, 1L, 1L));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create ParticipantInvestor - V1")
    void createParticipantInvestorV1() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(investorRepository.findById(anyLong())).thenReturn(Optional.of(createInvestorEntityTestData()));
        when(participantInvestorRepository.findById(any())).thenReturn(Optional.empty());
        when(participantInvestorRepository.save(any())).thenReturn(createParticipantInvestorEntityTestData());
        when(propertiesConfig.getApiName()).thenReturn("any string");

        ParticipantInvestorOutDto participantInvestorOutDto = participantInvestorServiceImpl
                .createParticipantInvestor(createParticipantInvestorInDtoTestData());

        assertNotNull(participantInvestorOutDto);
    }

    @Test
    @DisplayName("Create ParticipantInvestor exception - Version Api Not Found")
    void createParticipantInvestorExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ParticipantInvestorInDto participantInvestorInDto = createParticipantInvestorInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantInvestorServiceImpl.createParticipantInvestor(participantInvestorInDto));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create ParticipantInvestor exception - Participant Not Found - V1")
    void createParticipantInvestorV1ExceptionParticipantNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.empty());

        ParticipantInvestorInDto participantInvestorInDto = createParticipantInvestorInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantInvestorServiceImpl.createParticipantInvestor(participantInvestorInDto));

        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create ParticipantInvestor exception - Investor Not Found - V1")
    void createParticipantInvestorV1ExceptionInvestorNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(investorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ParticipantInvestorInDto participantInvestorInDto = createParticipantInvestorInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantInvestorServiceImpl.createParticipantInvestor(participantInvestorInDto));

        assertEquals(ErrorCode.INVESTOR_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create ParticipantInvestor exception - ParticipantInvestor exists - V1")
    void createParticipantInvestorV1ExceptionParticipantInvestorExistsTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(investorRepository.findById(anyLong())).thenReturn(Optional.of(createInvestorEntityTestData()));
        when(participantInvestorRepository.findById(any())).thenReturn(Optional.of(createParticipantInvestorEntityTestData()));

        ParticipantInvestorInDto participantInvestorInDto = createParticipantInvestorInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantInvestorServiceImpl.createParticipantInvestor(participantInvestorInDto));

        assertEquals(ErrorCode.PARTICIPANT_INVESTOR_EXISTS.name(), serviceException.getErrorCode().getName());
    }

    private ParticipantInvestorEntity createParticipantInvestorEntityTestData() {
        return ParticipantInvestorEntity.builder()
                .participant(createParticipantEntityTestData())
                .investor(createInvestorEntityTestData())
                .participant(createParticipantEntityTestData())
                .investor(createInvestorEntityTestData())
                .build();
    }

    private ParticipantEntity createParticipantEntityTestData() {
        return ParticipantEntity.builder()
                .id(1L)
                .build();
    }

    private InvestorEntity createInvestorEntityTestData() {
        return InvestorEntity.builder()
                .id(1L)
                .build();
    }

    private ParticipantInvestorInDto createParticipantInvestorInDtoTestData() {
        return ParticipantInvestorInDto.builder()
                .participantId(1L)
                .investorId(1L)
                .build();
    }

}
