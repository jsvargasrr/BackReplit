package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.util.ApplicationUtils;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantOutDto;
import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import com.nubo.masterparticipant.api.models.entity.CurrencyEntity;
import com.nubo.masterparticipant.api.models.entity.DepartmentEntity;
import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import com.nubo.masterparticipant.api.models.entity.TaxStatusEntity;
import com.nubo.masterparticipant.api.models.entity.TradeEntity;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import com.nubo.masterparticipant.api.models.enums.PropertiesEnum;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.CountryRepository;
import com.nubo.masterparticipant.api.repository.DepartmentRepository;
import com.nubo.masterparticipant.api.repository.IdentificationTypeRepository;
import com.nubo.masterparticipant.api.repository.ParticipantRepository;
import com.nubo.masterparticipant.api.repository.StatusRepository;
import com.nubo.masterparticipant.api.repository.TaxStatusRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private IdentificationTypeRepository identificationTypeRepository;

    @Mock
    private TaxStatusRepository taxStatusRepository;

    @Mock
    private PropertiesConfig propertiesConfig;

    @InjectMocks
    private ParticipantServiceImpl participantServiceImpl;

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
    @DisplayName("Find All Participant - V1")
    void findAllParticipantV1Test() {
        this.setVersion(VersionApiEnum.V1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ParticipantEntity> page = new PageImpl<>(List.of(createParticipantEntityTestData()), pageable, 1);
        when(participantRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        PageResponseOutDto<ParticipantOutDto> participantOutDtoList = participantServiceImpl.findAllParticipant(pageable, null);

        assertNotNull(participantOutDtoList);
        assertEquals(1, participantOutDtoList.getTotalElements());
    }

    @Test
    @DisplayName("Find All Participant exception - Version Api Not Found")
    void findAllParticipantExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        Pageable pageable = PageRequest.of(0, 10);
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.findAllParticipant(pageable, null));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Find Participant By Id successfully - V1")
    void findParticipantByIdV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));

        ParticipantOutDto participantOutDto = participantServiceImpl.findParticipantById(1L);

        assertNotNull(participantOutDto);
    }

    @Test
    @DisplayName("Find Participant By Id exception - Version Api Not Found")
    void findParticipantByIdExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.findParticipantById(1L));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create Participant successfully - V1")
    void createParticipantV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(createCountryEntityTestData()));
        when(identificationTypeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createIdentificationTypeEntityTestData()));
        when(taxStatusRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createTaxStatusEntityTestData()));
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(createTypeEntityTestData()));
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(createStatusEntityTestData()));
        when(departmentRepository.findById(any())).thenReturn(Optional.of(createDepartmentEntityTestData()));
        when(propertiesConfig.getApiName()).thenReturn("any string");
        when(participantRepository.save(any(ParticipantEntity.class))).thenReturn(createParticipantEntityTestData());

        ParticipantOutDto participantOutDto = participantServiceImpl.createParticipant(createParticipantInDtoTestData());

        assertNotNull(participantOutDto);
    }

    @Test
    @DisplayName("Create Participant exception - Version Api Not Found")
    void createParticipantExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ParticipantInDto participantInDto = createParticipantInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.createParticipant(participantInDto));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Create Participant exception - Country Not Found - V1")
    void createParticipantExceptionCountryNotFoundV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(countryRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(createTypeEntityTestData()));
        when(identificationTypeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createIdentificationTypeEntityTestData()));

        ParticipantInDto participantInDto = createParticipantInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.createParticipant(participantInDto));

        assertEquals(ErrorCode.COUNTRY_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update Participant successfully - V1")
    void updateParticipantV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(countryRepository.findById(anyLong())).thenReturn(Optional.of(createCountryEntityTestData()));
        when(identificationTypeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createIdentificationTypeEntityTestData()));
        when(taxStatusRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createTaxStatusEntityTestData()));
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(createTypeEntityTestData()));
        when(statusRepository.findById(anyLong())).thenReturn(Optional.of(createStatusEntityTestData()));
        when(departmentRepository.findById(any())).thenReturn(Optional.of(createDepartmentEntityTestData()));
        when(propertiesConfig.getApiName()).thenReturn("any string");
        when(participantRepository.save(any(ParticipantEntity.class))).thenReturn(createParticipantEntityTestData());

        ParticipantOutDto participantOutDto = participantServiceImpl.updateParticipant(1L, createParticipantUpdateInDtoTestData());

        assertNotNull(participantOutDto);

        ParticipantUpdateInDto participantUpdateInDto = ParticipantUpdateInDto.builder().build();
        participantOutDto = participantServiceImpl.updateParticipant(anyLong(), participantUpdateInDto);

        assertNotNull(participantOutDto);
    }

    @Test
    @DisplayName("Update Participant exception - Version Api Not Found")
    void updateParticipantExceptionVersionApiNotFoundTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        ParticipantUpdateInDto participantUpdateInDto = createParticipantUpdateInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.updateParticipant(1L, participantUpdateInDto));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update Participant exception - Participant not found - V1")
    void updateParticipantExceptionParticipantNotFoundV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.empty());

        ParticipantUpdateInDto participantUpdateInDto = createParticipantUpdateInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.updateParticipant(1L, participantUpdateInDto));

        assertEquals(ErrorCode.PARTICIPANT_NOT_FOUND.name(), serviceException.getErrorCode().getName());
    }

    @Test
    @DisplayName("Update Participant exception - Country not found - V1")
    void updateParticipantExceptionCountryNotFoundV1Test() {
        this.setVersion(VersionApiEnum.V1);

        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(createParticipantEntityTestData()));
        when(typeRepository.findById(anyLong())).thenReturn(Optional.of(createTypeEntityTestData()));
        when(countryRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(identificationTypeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(createIdentificationTypeEntityTestData()));

        ParticipantUpdateInDto participantUpdateInDto = createParticipantUpdateInDtoTestData();
        ServiceException serviceException = assertThrows(ServiceException.class, () ->
                participantServiceImpl.updateParticipant(1L, participantUpdateInDto));

        assertEquals(ErrorCode.COUNTRY_NOT_FOUND.name(), serviceException.getErrorCode().getName());
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

    private ParticipantInDto createParticipantInDtoTestData() {
        String anyString = "string";
        String anyCharacter = "A";
        return ParticipantInDto.builder()
                .code(anyString)
                .documentNumber(anyString)
                .identificationTypeId(1L)
                .name(anyString)
                .shortName(anyString)
                .typeId(1L)
                .mnemonic("test")
                .countryId(1L)
                .registrationDate(LocalDate.now())
                .taxCode(anyString)
                .departmentId(anyString)
                .mainAddress(anyString)
                .webPage(anyString)
                .phoneNumber(anyString)
                .statusId(1L)
                .nitClearingHouse("st")
                .taxStatusId(1L)
                .typeEntity(anyCharacter)
                .marginAccount(anyCharacter)
                .masterAccount(anyCharacter)
                .directAllocation(anyCharacter)
                .marketMaker(anyCharacter)
                .enabled(true)
                .custodian(true)
                .build();
    }

    private ParticipantUpdateInDto createParticipantUpdateInDtoTestData() {
        String anyString = "string";
        String anyCharacter = "A";
        return ParticipantUpdateInDto.builder()
                .code(anyString)
                .documentNumber(anyString)
                .identificationTypeId(1L)
                .name(anyString)
                .shortName(anyString)
                .typeId(1L)
                .mnemonic("test")
                .countryId(1L)
                .registrationDate(LocalDate.now())
                .taxCode(anyString)
                .departmentId(anyString)
                .mainAddress(anyString)
                .webPage(anyString)
                .phoneNumber(anyString)
                .statusId(1L)
                .nitClearingHouse("st")
                .taxStatusId(1L)
                .typeEntity(anyCharacter)
                .marginAccount(anyCharacter)
                .masterAccount(anyCharacter)
                .directAllocation(anyCharacter)
                .marketMaker(anyCharacter)
                .enabled(true)
                .custodian(true)
                .build();
    }

    @Test
    @DisplayName("Get Participant by Trade - Service")
    void getParticipantByTradeTest() {
        this.setVersion(VersionApiEnum.V1);

        Long tradeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<ParticipantEntity> page = new PageImpl<>(List.of(createParticipantEntityTestData()), pageable, 1);

        when(tradeRepository.findById(anyLong())).thenReturn(Optional.of(createTradeEntityTestData()));
        when(participantRepository.findParticipantsByTradeId(tradeId, pageable)).thenReturn(page);

        List<ParticipantOutDto> result = participantServiceImpl.getParticipantByTrade(tradeId, pageable);

        assertNotNull(result);
        assertEquals(page.getTotalElements(), result.size());
        assertEquals(page.getContent().get(0).getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Get Participant by Trade - Invalid Version")
    void getParticipantByTradeInvalidVersionTest() {
        this.setVersion(VersionApiEnum.VERSION_NOT_IMPLEMENTED);

        Long tradeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        ServiceException exception = assertThrows(ServiceException.class, () ->
                participantServiceImpl.getParticipantByTrade(tradeId, pageable));

        assertEquals(ErrorCode.VERSION_API_NOT_FOUND.name(), exception.getErrorCode().getName());
    }

    private TradeEntity createTradeEntityTestData() {
        return TradeEntity.builder()
                .id(1L)
                .participantIdBuy(1L)
                .participantIdSell(1L)
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
