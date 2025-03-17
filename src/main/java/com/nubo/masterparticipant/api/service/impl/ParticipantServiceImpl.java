package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperPageUtil;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantOutDto;
import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import com.nubo.masterparticipant.api.models.entity.DepartmentEntity;
import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import com.nubo.masterparticipant.api.models.entity.TaxStatusEntity;
import com.nubo.masterparticipant.api.models.entity.TradeEntity;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.CountryRepository;
import com.nubo.masterparticipant.api.repository.DepartmentRepository;
import com.nubo.masterparticipant.api.repository.IdentificationTypeRepository;
import com.nubo.masterparticipant.api.repository.ParticipantRepository;
import com.nubo.masterparticipant.api.repository.StatusRepository;
import com.nubo.masterparticipant.api.repository.TaxStatusRepository;
import com.nubo.masterparticipant.api.repository.TradeRepository;
import com.nubo.masterparticipant.api.repository.TypeRepository;
import com.nubo.masterparticipant.api.service.ParticipantService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final CountryRepository countryRepository;
    private final TradeRepository tradeRepository;
    private final TypeRepository typeRepository;
    private final DepartmentRepository departmentRepository;
    private final StatusRepository statusRepository;
    private final IdentificationTypeRepository identificationTypeRepository;
    private final TaxStatusRepository taxStatusRepository;
    private final PropertiesConfig propertiesConfig;

    @Override
    public PageResponseOutDto<ParticipantOutDto> findAllParticipant(Pageable pageable, Long statusId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllParticipantV1(pageable, statusId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private PageResponseOutDto<ParticipantOutDto> findAllParticipantV1(Pageable pageable, Long statusId) {
        log.info("Start method findAllParticipantV1 with page: {} and size: {}", pageable.getPageNumber(),
                pageable.getPageSize());

        Specification<ParticipantEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(statusId)) {
                predicates.add(criteriaBuilder.equal(root.get("status").get("id"), statusId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ParticipantEntity> participantPage = participantRepository.findAll(specification, pageable);
        List<ParticipantOutDto> participantOutDtoList = participantPage.getContent().stream()
                .map(MapperUtil::participantEntityToParticipantOutDto)
                .toList();

        log.debug("End method findAllParticipantV1, number of participant found: {}", participantOutDtoList.size());
        return MapperPageUtil.pageToPageResponseOutDto(participantOutDtoList, participantPage);
    }

    @Override
    public ParticipantOutDto findParticipantById(Long participantId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findParticipantByIdV1(participantId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantOutDto findParticipantByIdV1(Long participantId) {
        log.info("Start method findParticipantByIdV1, participantId -> {}", participantId);

        ParticipantEntity participantEntity = validateParticipant(participantId);
        ParticipantOutDto participantOutDto = MapperUtil.participantEntityToParticipantOutDto(participantEntity);

        log.info("End method findParticipantByIdV1, participant -> {}", participantOutDto);
        return participantOutDto;
    }

    @Override
    public List<ParticipantOutDto> getParticipantByTrade(Long tradeId, Pageable pageable) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return getParticipantByTradeV1(tradeId, pageable);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<ParticipantOutDto> getParticipantByTradeV1(Long tradeId, Pageable pageable) {
        log.info("Start method getParticipantByTradeV1, tradeId -> {}", tradeId);

        TradeEntity tradeEntity = validateTrade(tradeId);
        List<ParticipantOutDto> participantOutDto = participantRepository.findParticipantsByTradeId(tradeEntity.getId(), pageable)
                .stream()
                .map(MapperUtil::participantEntityToParticipantOutDto)
                .toList();

        log.info("End method getParticipantByTradeV1, tradeId -> {}", participantOutDto);
        return participantOutDto;
    }

    @Override
    public ParticipantOutDto createParticipant(ParticipantInDto participantInDto) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return createParticipantV1(participantInDto);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantOutDto createParticipantV1(ParticipantInDto participantInDto) {
        log.debug("Start method createParticipantV1, participant -> {}", participantInDto);

        ParticipantEntity participantEntity = ParticipantEntity.builder()
                .code(participantInDto.getCode())
                .documentNumber(participantInDto.getDocumentNumber())
                .identificationType(validateIdentificationType(participantInDto.getIdentificationTypeId()))
                .name(participantInDto.getName())
                .shortName(participantInDto.getShortName())
                .type(validateType(participantInDto.getTypeId()))
                .mnemonic(participantInDto.getMnemonic())
                .country(validateCountry(participantInDto.getCountryId()))
                .registrationDate(participantInDto.getRegistrationDate())
                .taxCode(participantInDto.getTaxCode())
                .department(validateDepartment(participantInDto.getDepartmentId()))
                .mainAddress(participantInDto.getMainAddress())
                .webPage(participantInDto.getWebPage())
                .phoneNumber(participantInDto.getPhoneNumber())
                .status(validateStatus(participantInDto.getStatusId()))
                .nitClearingHouse(participantInDto.getNitClearingHouse())
                .taxStatus(validateTaxStatus(participantInDto.getTaxStatusId()))
                .typeEntity(participantInDto.getTypeEntity())
                .marginAccount(participantInDto.getMarginAccount())
                .masterAccount(participantInDto.getMasterAccount())
                .directAllocation(participantInDto.getDirectAllocation())
                .marketMaker(participantInDto.getMarketMaker())
                .enabled(participantInDto.getEnabled())
                .custodian(participantInDto.getCustodian())
                .lastModifyApplication(propertiesConfig.getApiName())
                .lastModifyIp(PropertiesContext.getClientIp())
                .lastModifyUser(PropertiesContext.getClientUser())
                .lastModifyDate(PropertiesContext.getClientDatetime())
                .build();

        ParticipantOutDto participantOutDto = MapperUtil.participantEntityToParticipantOutDto(
                participantRepository.save(participantEntity));

        log.info("End method createParticipantV1, participant -> {}", participantOutDto);
        return participantOutDto;
    }

    @Override
    public ParticipantOutDto updateParticipant(Long participantId, ParticipantUpdateInDto participantUpdateInDto) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return updateParticipantV1(participantId, participantUpdateInDto);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantOutDto updateParticipantV1(Long participantId, ParticipantUpdateInDto participantUpdateInDto) {
        log.debug("Start method updateParticipantV1, participantId -> {}, participant -> {}", participantId, participantUpdateInDto);

        ParticipantEntity participantEntityLast = validateParticipant(participantId);

        ParticipantEntity participantEntity = ParticipantEntity.builder()
                .id(participantEntityLast.getId())
                .code(ObjectUtils.isEmpty(participantUpdateInDto.getCode()) ?
                        participantEntityLast.getCode() : participantUpdateInDto.getCode())
                .documentNumber(ObjectUtils.isEmpty(participantUpdateInDto.getDocumentNumber()) ?
                        participantEntityLast.getDocumentNumber() : participantUpdateInDto.getDocumentNumber())
                .identificationType(ObjectUtils.isEmpty(participantUpdateInDto.getIdentificationTypeId()) ?
                        participantEntityLast.getIdentificationType()
                        : validateIdentificationType(participantUpdateInDto.getIdentificationTypeId()))
                .name(ObjectUtils.isEmpty(participantUpdateInDto.getName()) ?
                        participantEntityLast.getName() : participantUpdateInDto.getName())
                .shortName(ObjectUtils.isEmpty(participantUpdateInDto.getShortName()) ?
                        participantEntityLast.getShortName() : participantUpdateInDto.getShortName())
                .type(ObjectUtils.isEmpty(participantUpdateInDto.getTypeId()) ?
                        participantEntityLast.getType() : validateType(participantUpdateInDto.getTypeId()))
                .mnemonic(ObjectUtils.isEmpty(participantUpdateInDto.getMnemonic()) ?
                        participantEntityLast.getMnemonic() : participantUpdateInDto.getMnemonic())
                .country(ObjectUtils.isEmpty(participantUpdateInDto.getCountryId()) ?
                        participantEntityLast.getCountry() : validateCountry(participantUpdateInDto.getCountryId()))
                .registrationDate(ObjectUtils.isEmpty(participantUpdateInDto.getRegistrationDate()) ?
                        participantEntityLast.getRegistrationDate() : participantUpdateInDto.getRegistrationDate())
                .taxCode(ObjectUtils.isEmpty(participantUpdateInDto.getTaxCode()) ?
                        participantEntityLast.getTaxCode() : participantUpdateInDto.getTaxCode())
                .department(ObjectUtils.isEmpty(participantUpdateInDto.getDepartmentId()) ?
                        participantEntityLast.getDepartment() : validateDepartment(participantUpdateInDto.getDepartmentId()))
                .mainAddress(ObjectUtils.isEmpty(participantUpdateInDto.getMainAddress()) ?
                        participantEntityLast.getMainAddress() : participantUpdateInDto.getMainAddress())
                .webPage(ObjectUtils.isEmpty(participantUpdateInDto.getWebPage()) ?
                        participantEntityLast.getWebPage() : participantUpdateInDto.getWebPage())
                .phoneNumber(ObjectUtils.isEmpty(participantUpdateInDto.getPhoneNumber()) ?
                        participantEntityLast.getPhoneNumber() : participantUpdateInDto.getPhoneNumber())
                .status(ObjectUtils.isEmpty(participantUpdateInDto.getStatusId()) ?
                        participantEntityLast.getStatus() : validateStatus(participantUpdateInDto.getStatusId()))
                .nitClearingHouse(ObjectUtils.isEmpty(participantUpdateInDto.getNitClearingHouse()) ?
                        participantEntityLast.getNitClearingHouse() : participantUpdateInDto.getNitClearingHouse())
                .taxStatus(ObjectUtils.isEmpty(participantUpdateInDto.getTaxStatusId()) ?
                        participantEntityLast.getTaxStatus() : validateTaxStatus(participantUpdateInDto.getTaxStatusId()))
                .typeEntity(ObjectUtils.isEmpty(participantUpdateInDto.getTypeEntity()) ?
                        participantEntityLast.getTypeEntity() : participantUpdateInDto.getTypeEntity())
                .marginAccount(ObjectUtils.isEmpty(participantUpdateInDto.getMarginAccount()) ?
                        participantEntityLast.getMarginAccount() : participantUpdateInDto.getMarginAccount())
                .masterAccount(ObjectUtils.isEmpty(participantUpdateInDto.getMasterAccount()) ?
                        participantEntityLast.getMasterAccount() : participantUpdateInDto.getMasterAccount())
                .directAllocation(ObjectUtils.isEmpty(participantUpdateInDto.getDirectAllocation()) ?
                        participantEntityLast.getDirectAllocation() : participantUpdateInDto.getDirectAllocation())
                .marketMaker(ObjectUtils.isEmpty(participantUpdateInDto.getMarketMaker()) ?
                        participantEntityLast.getMarketMaker() : participantUpdateInDto.getMarketMaker())
                .enabled(ObjectUtils.isEmpty(participantUpdateInDto.getEnabled()) ?
                        participantEntityLast.getEnabled() : participantUpdateInDto.getEnabled())
                .custodian(ObjectUtils.isEmpty(participantUpdateInDto.getCustodian()) ?
                        participantEntityLast.getCustodian() : participantUpdateInDto.getCustodian())
                .lastModifyApplication(propertiesConfig.getApiName())
                .lastModifyIp(PropertiesContext.getClientIp())
                .lastModifyUser(PropertiesContext.getClientUser())
                .lastModifyDate(PropertiesContext.getClientDatetime())
                .build();

        ParticipantOutDto participantOutDto = MapperUtil.participantEntityToParticipantOutDto(
                participantRepository.save(participantEntity));

        log.info("End method updateParticipantV1, participant -> {}", participantOutDto);
        return participantOutDto;
    }

    private CountryEntity validateCountry(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ServiceException(ErrorCode.COUNTRY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private ParticipantEntity validateParticipant(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new ServiceException(ErrorCode.PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private TradeEntity validateTrade(Long tradeId) {
        return tradeRepository.findById(tradeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.TRADE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private TypeEntity validateType(Long typeId) {
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new ServiceException(ErrorCode.TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private DepartmentEntity validateDepartment(String departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ServiceException(ErrorCode.DEPARTMENT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private StatusEntity validateStatus(Long statusId) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new ServiceException(ErrorCode.STATUS_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private IdentificationTypeEntity validateIdentificationType(Long id) {
        return identificationTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.IDENTIFICATION_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private TaxStatusEntity validateTaxStatus(Long id) {
        return taxStatusRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.TAX_STATUS_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

}
