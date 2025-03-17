package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.util.ApplicationUtils;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantAccountOutDto;
import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import com.nubo.masterparticipant.api.models.entity.CurrencyEntity;
import com.nubo.masterparticipant.api.models.entity.DepartmentEntity;
import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantAccountEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import com.nubo.masterparticipant.api.models.entity.TaxStatusEntity;
import com.nubo.masterparticipant.api.models.entity.TradeEntity;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.ParticipantAccountRepository;
import com.nubo.masterparticipant.api.repository.ParticipantRepository;
import com.nubo.masterparticipant.api.repository.StatusRepository;
import com.nubo.masterparticipant.api.repository.TradeRepository;
import com.nubo.masterparticipant.api.repository.TypeRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantAccountServiceImplTest {

    @Mock
    private ParticipantAccountRepository participantAccountRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private PropertiesConfig propertiesConfig;

    @InjectMocks
    private ParticipantAccountServiceImpl participantAccountServiceImpl;

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
    @DisplayName("Find All ParticipantAccount - V1")
    void findAllParticipantAccountV1() {
        this.setVersion(VersionApiEnum.V1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ParticipantAccountEntity> page = new PageImpl<>(List.of(createParticipantAccountEntityTestData()), pageable, 1);
        when(participantAccountRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        PageResponseOutDto<ParticipantAccountOutDto> participantAccountOutDtoPage = participantAccountServiceImpl
                .findAllParticipantAccount(pageable, 1L);

        assertNotNull(participantAccountOutDtoPage);
        assertEquals(1, participantAccountOutDtoPage.getTotalElements());
    }

    @Test
    @DisplayName("Find All ParticipantAccount exception - Version Api Not Found")
    void findAllParticipantAccountExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        Pageable pageable = PageRequest.of(0, 10);
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.findAllParticipantAccount(pageable, 1L));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Find ParticipantAccount by Id - V1")
    void findParticipantAccountByIdV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantAccountRepository.findById(anyLong()))
                .thenReturn(Optional.of(createParticipantAccountEntityTestData()));

        ParticipantAccountOutDto participantAccountOutDto = participantAccountServiceImpl
                .findParticipantAccountById(1L);

        assertNotNull(participantAccountOutDto);
    }

    @Test
    @DisplayName("Find ParticipantAccount by Id exception - Version Api Not Found")
    void findParticipantAccountByIdExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.findParticipantAccountById(1L));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Find ParticipantAccount by Id exception - ParticipantAccount Not Found - V1")
    void findParticipantAccountByIdV1ExceptionParticipantAccountNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantAccountRepository.findById(anyLong())).thenReturn(Optional.empty());

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.findParticipantAccountById(1L));

        assertEquals(ErrorCode.PARTICIPANT_ACCOUNT_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create ParticipantAccount - V1")
    void createParticipantAccountV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(createStatusEntityTestData()));
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(createTypeEntityTestData()));
        when(propertiesConfig.getApiName()).thenReturn("any string");
        when(participantAccountRepository.save(any())).thenReturn(createParticipantAccountEntityTestData());

        ParticipantAccountOutDto participantAccountOutDto = participantAccountServiceImpl
                .createParticipantAccount(createParticipantAccountInDtoTestData());

        assertNotNull(participantAccountOutDto);
    }

    @Test
    @DisplayName("Create ParticipantAccount exception - Version Api Not Found")
    void createParticipantAccountExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ParticipantAccountInDto participantAccountInDto = createParticipantAccountInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.createParticipantAccount(participantAccountInDto));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create ParticipantAccount exception - Participant Not Found - V1")
    void createParticipantAccountV1ExceptionParticipantNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.empty());

        ParticipantAccountInDto participantAccountInDto = createParticipantAccountInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.createParticipantAccount(participantAccountInDto));

        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update ParticipantAccount - V1")
    void updateParticipantAccountV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantAccountRepository.findById(any())).thenReturn(Optional.of(createParticipantAccountEntityTestData()));
        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(createStatusEntityTestData()));
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(createTypeEntityTestData()));
        when(propertiesConfig.getApiName()).thenReturn("any string");
        when(participantAccountRepository.save(any())).thenReturn(createParticipantAccountEntityTestData());

        ParticipantAccountOutDto participantAccountOutDto = participantAccountServiceImpl
                .updateParticipantAccount(1L, createParticipantAccountUpdateInDtoTestData());

        assertNotNull(participantAccountOutDto);
    }

    @Test
    @DisplayName("Update ParticipantAccount exception - Version Api Not Found")
    void updateParticipantAccountExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ParticipantAccountUpdateInDto participantAccountUpdateInDto = createParticipantAccountUpdateInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.updateParticipantAccount(1L, participantAccountUpdateInDto));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update ParticipantAccount by Id exception - ParticipantAccount Not Found - V1")
    void updateParticipantAccountV1ExceptionParticipantAccountNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantAccountRepository.findById(any())).thenReturn(Optional.empty());

        ParticipantAccountUpdateInDto participantAccountUpdateInDto = createParticipantAccountUpdateInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.updateParticipantAccount(1L, participantAccountUpdateInDto));

        assertEquals(ErrorCode.PARTICIPANT_ACCOUNT_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update ParticipantAccount exception - Participant Not Found - V1")
    void updateParticipantAccountV1ExceptionParticipantNotFoundTest() {
        this.setVersion(VersionApiEnum.V1);

        when(participantAccountRepository.findById(any())).thenReturn(Optional.of(createParticipantAccountEntityTestData()));
        when(participantRepository.findById(anyLong())).thenReturn(Optional.empty());

        ParticipantAccountUpdateInDto participantAccountUpdateInDto = createParticipantAccountUpdateInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.updateParticipantAccount(1L, participantAccountUpdateInDto));

        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Get Participant Accounts by Trade - Success")
    void getParticipantAccountsByTradeSuccessTest() {
        this.setVersion(VersionApiEnum.V1);

        Long tradeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<ParticipantAccountEntity> participantAccountEntities = List.of(createParticipantAccountEntityTestData());

        when(tradeRepository.findById(anyLong())).thenReturn(Optional.of(createTradeEntityTestData()));
        when(participantAccountRepository.findParticipantAccountsByTradeId(tradeId, pageable))
                .thenReturn(new PageImpl<>(participantAccountEntities));

        List<ParticipantAccountOutDto> result = participantAccountServiceImpl.getParticipantAccountsByTrade(tradeId, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(participantAccountEntities.get(0).getId(), result.get(0).getId());
        assertEquals(participantAccountEntities.get(0).getAccountNumber(), result.get(0).getAccountNumber());
    }

    @Test
    @DisplayName("Get Participant Accounts by Trade - Empty Result")
    void getParticipantAccountsByTradeEmptyResultTest() {
        this.setVersion(VersionApiEnum.V1);
        Long tradeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(tradeRepository.findById(anyLong())).thenReturn(Optional.of(createTradeEntityTestData()));
        when(participantAccountRepository.findParticipantAccountsByTradeId(tradeId, pageable))
                .thenReturn(Page.empty());

        List<ParticipantAccountOutDto> result = participantAccountServiceImpl.getParticipantAccountsByTrade(tradeId, pageable);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Get Participant Accounts by Trade - Invalid Trade Id")
    void getParticipantAccountsByTradeInvalidTradeIdTest() {
        this.setVersion(VersionApiEnum.V1);

        Long tradeId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        when(tradeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () ->
                participantAccountServiceImpl.getParticipantAccountsByTrade(tradeId, pageable));

        assertEquals(ErrorCode.TRADE_NOT_FOUND, exception.getErrorCode());
    }

    private TradeEntity createTradeEntityTestData() {
        return TradeEntity.builder()
                .id(1L)
                .participantIdBuy(1L)
                .participantIdSell(1L)
                .build();
    }

    private ParticipantAccountEntity createParticipantAccountEntityTestData() {
        return ParticipantAccountEntity.builder()
                .id(1L)
                .participant(createParticipantEntityTestData())
                .accountNumber("123456789")
                .status(createStatusEntityTestData())
                .type(createTypeEntityTestData())
                .lastModifyUser("TestUser")
                .lastModifyApplication("TestApplication")
                .lastModifyDate(LocalDateTime.now())
                .lastModifyIp("127.0.0.1")
                .build();
    }

    private ParticipantAccountInDto createParticipantAccountInDtoTestData() {
        return ParticipantAccountInDto.builder()
                .participantId(1L)
                .accountNumber("123456789")
                .statusId(1L)
                .typeId(1L)
                .build();
    }

    private ParticipantAccountUpdateInDto createParticipantAccountUpdateInDtoTestData() {
        return ParticipantAccountUpdateInDto.builder()
                .participantId(1L)
                .accountNumber("123456789")
                .statusId(1L)
                .typeId(1L)
                .build();
    }

    private ParticipantEntity createParticipantEntityTestData() {
        String anyString = "string";
        String anyCharacter = "A";
        return ParticipantEntity.builder()
                .id(1L)
                .code(anyString)
                .documentNumber(anyString)
                .identificationType(createIdentificationTypeEntityTestData())
                .name(anyString)
                .shortName(anyString)
                .type(createTypeEntityTestData())
                .mnemonic("test")
                .country(createCountryEntityTestData())
                .registrationDate(LocalDate.now())
                .taxCode(anyString)
                .department(createDepartmentEntityTestData())
                .mainAddress(anyString)
                .webPage(anyString)
                .phoneNumber(anyString)
                .status(createStatusEntityTestData())
                .nitClearingHouse("st")
                .taxStatus(createTaxStatusEntityTestData())
                .typeEntity(anyCharacter)
                .marginAccount(anyCharacter)
                .directAllocation(anyCharacter)
                .marketMaker(anyCharacter)
                .masterAccount(anyCharacter)
                .enabled(true)
                .custodian(true)
                .build();
    }

    private IdentificationTypeEntity createIdentificationTypeEntityTestData() {
        return IdentificationTypeEntity.builder()
                .id(1L)
                .description("s")
                .build();
    }

    private TaxStatusEntity createTaxStatusEntityTestData() {
        return TaxStatusEntity.builder()
                .id(1L)
                .description("s")
                .build();
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

    private StatusEntity createStatusEntityTestData() {
        return StatusEntity.builder()
                .id(1L)
                .description("")
                .build();
    }

    private TypeEntity createTypeEntityTestData() {
        String anyString = "any string";
        return TypeEntity.builder()
                .id(1L)
                .description(anyString)
                .category(anyString)
                .build();
    }

    private DepartmentEntity createDepartmentEntityTestData() {
        String anyString = "any";
        return DepartmentEntity.builder()
                .id(anyString)
                .description(anyString)
                .build();
    }

}
