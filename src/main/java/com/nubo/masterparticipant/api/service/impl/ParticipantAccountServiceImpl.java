package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperPageUtil;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantAccountOutDto;
import com.nubo.masterparticipant.api.models.entity.ParticipantAccountEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import com.nubo.masterparticipant.api.models.entity.TradeEntity;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.ParticipantAccountRepository;
import com.nubo.masterparticipant.api.repository.ParticipantRepository;
import com.nubo.masterparticipant.api.repository.StatusRepository;
import com.nubo.masterparticipant.api.repository.TradeRepository;
import com.nubo.masterparticipant.api.repository.TypeRepository;
import com.nubo.masterparticipant.api.service.ParticipantAccountService;
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
public class ParticipantAccountServiceImpl implements ParticipantAccountService {

    private final ParticipantAccountRepository participantAccountRepository;
    private final ParticipantRepository participantRepository;
    private final TradeRepository tradeRepository;
    private final StatusRepository statusRepository;
    private final TypeRepository typeRepository;
    private final PropertiesConfig propertiesConfig;

    @Override
    public PageResponseOutDto<ParticipantAccountOutDto> findAllParticipantAccount(Pageable pageable, Long statusId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllParticipantAccountV1(pageable, statusId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private PageResponseOutDto<ParticipantAccountOutDto> findAllParticipantAccountV1(Pageable pageable, Long statusId) {
        log.info("Start method findAllParticipantAccountV1 with page: {} and size: {}", pageable.getPageNumber(),
                pageable.getPageSize());

        Specification<ParticipantAccountEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!ObjectUtils.isEmpty(statusId)) {
                predicates.add(criteriaBuilder.equal(root.get("status").get("id"), statusId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ParticipantAccountEntity> participantAccountEntityPage = participantAccountRepository.findAll(specification, pageable);

        List<ParticipantAccountOutDto> participantAccountOutDtoList = participantAccountEntityPage.getContent().stream()
                .map(MapperUtil::participantAccountEntityToParticipantAccountOutDto)
                .toList();

        log.debug("End method findAllParticipantAccountV1, number of participant Account found: {}", participantAccountOutDtoList.size());
        return MapperPageUtil.pageToPageResponseOutDto(participantAccountOutDtoList, participantAccountEntityPage);
    }

    @Override
    public ParticipantAccountOutDto findParticipantAccountById(Long participantAccountId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findParticipantAccountByIdV1(participantAccountId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantAccountOutDto findParticipantAccountByIdV1(Long participantAccountId) {
        log.info("Start method findParticipantAccountByIdV1, participantAccountId -> {}", participantAccountId);

        ParticipantAccountEntity participantAccountEntity = validateParticipantAccount(participantAccountId);
        ParticipantAccountOutDto participantAccountOutDto = MapperUtil.participantAccountEntityToParticipantAccountOutDto(
                participantAccountEntity);

        log.info("End method findParticipantAccountByIdV1, participantAccount -> {}", participantAccountOutDto);
        return participantAccountOutDto;
    }

    @Override
    public List<ParticipantAccountOutDto> getParticipantAccountsByTrade(Long tradeId, Pageable pageable) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return getParticipantAccountsByTradeV1(tradeId, pageable);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<ParticipantAccountOutDto> getParticipantAccountsByTradeV1(Long tradeId, Pageable pageable) {
        log.info("Start method getParticipantAccountsByTradeV1, tradeId -> {}", tradeId);

        TradeEntity tradeEntity = validateTrade(tradeId);
        List<ParticipantAccountOutDto> participantAccounts = participantAccountRepository
                .findParticipantAccountsByTradeId(tradeEntity.getId(), pageable)
                .stream()
                .map(MapperUtil::participantAccountEntityToParticipantAccountOutDto)
                .toList();

        log.info("End method getParticipantAccountsByTradeV1, tradeId -> {}", participantAccounts);
        return participantAccounts;
    }

    @Override
    public ParticipantAccountOutDto createParticipantAccount(ParticipantAccountInDto participantAccountInDto) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return createParticipantAccountV1(participantAccountInDto);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantAccountOutDto createParticipantAccountV1(ParticipantAccountInDto participantAccountInDto) {
        log.debug("Start method createParticipantAccountV1, participantAccount -> {}", participantAccountInDto);

        ParticipantAccountEntity participantAccountEntity = ParticipantAccountEntity.builder()
                .participant(validateParticipant(participantAccountInDto.getParticipantId()))
                .accountNumber(participantAccountInDto.getAccountNumber())
                .status(validateStatus(participantAccountInDto.getStatusId()))
                .type(validateType(participantAccountInDto.getTypeId()))
                .lastModifyApplication(propertiesConfig.getApiName())
                .lastModifyIp(PropertiesContext.getClientIp())
                .lastModifyUser(PropertiesContext.getClientUser())
                .lastModifyDate(PropertiesContext.getClientDatetime())
                .build();

        ParticipantAccountOutDto participantAccountOutDto = MapperUtil.participantAccountEntityToParticipantAccountOutDto(
                participantAccountRepository.save(participantAccountEntity));

        log.info("End method createParticipantAccountV1, participantAccount -> {}", participantAccountOutDto);

        return participantAccountOutDto;
    }

    @Override
    public ParticipantAccountOutDto updateParticipantAccount(Long participantAccountId,
                                                             ParticipantAccountUpdateInDto participantAccountUpdateInDto) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return updateParticipantAccountV1(participantAccountId, participantAccountUpdateInDto);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantAccountOutDto updateParticipantAccountV1(Long participantAccountId,
                                                                ParticipantAccountUpdateInDto participantAccountUpdateInDto) {
        log.debug("Start method updateParticipantAccountV1, participantAccountId -> {}, participantAccount -> {}",
                participantAccountId, participantAccountUpdateInDto);

        ParticipantAccountEntity participantAccountEntityLast = validateParticipantAccount(participantAccountId);

        ParticipantAccountEntity participantAccountEntity = ParticipantAccountEntity.builder()
                .id(participantAccountEntityLast.getId())
                .participant(ObjectUtils.isEmpty(participantAccountUpdateInDto.getParticipantId()) ?
                        participantAccountEntityLast.getParticipant() : validateParticipant(participantAccountUpdateInDto.getParticipantId()))
                .accountNumber(ObjectUtils.isEmpty(participantAccountUpdateInDto.getAccountNumber()) ?
                        participantAccountEntityLast.getAccountNumber() : participantAccountUpdateInDto.getAccountNumber())
                .status(ObjectUtils.isEmpty(participantAccountUpdateInDto.getStatusId()) ?
                        participantAccountEntityLast.getStatus() : validateStatus(participantAccountUpdateInDto.getStatusId()))
                .type(ObjectUtils.isEmpty(participantAccountUpdateInDto.getTypeId()) ?
                        participantAccountEntityLast.getType() : validateType(participantAccountUpdateInDto.getTypeId()))
                .lastModifyApplication(propertiesConfig.getApiName())
                .lastModifyIp(PropertiesContext.getClientIp())
                .lastModifyUser(PropertiesContext.getClientUser())
                .lastModifyDate(PropertiesContext.getClientDatetime())
                .build();

        ParticipantAccountOutDto participantAccountOutDto = MapperUtil.participantAccountEntityToParticipantAccountOutDto(
                participantAccountRepository.save(participantAccountEntity));

        log.info("End method updateParticipantAccountV1, participantAccount -> {}", participantAccountOutDto);
        return participantAccountOutDto;
    }

    private ParticipantAccountEntity validateParticipantAccount(Long participantAccountId) {
        return participantAccountRepository.findById(participantAccountId)
                .orElseThrow(() -> new ServiceException(ErrorCode.PARTICIPANT_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));
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

    private StatusEntity validateStatus(Long statusId) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new ServiceException(ErrorCode.STATUS_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

}
