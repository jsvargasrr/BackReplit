package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperPageUtil;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantInvestorInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantInvestorOutDto;
import com.nubo.masterparticipant.api.models.entity.InvestorEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantInvestorEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantInvestorKey;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.InvestorRepository;
import com.nubo.masterparticipant.api.repository.ParticipantInvestorRepository;
import com.nubo.masterparticipant.api.repository.ParticipantRepository;
import com.nubo.masterparticipant.api.service.ParticipantInvestorService;
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
public class ParticipantInvestorServiceImpl implements ParticipantInvestorService {

    private final ParticipantInvestorRepository participantInvestorRepository;
    private final ParticipantRepository participantRepository;
    private final InvestorRepository investorRepository;
    private final PropertiesConfig propertiesConfig;

    @Override
    public PageResponseOutDto<ParticipantInvestorOutDto> findAllParticipantInvestor(Pageable pageable, Long participantId, Long investorId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllParticipantInvestorV1(pageable, participantId, investorId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private PageResponseOutDto<ParticipantInvestorOutDto> findAllParticipantInvestorV1(Pageable pageable, Long participantId, Long investorId) {
        log.info("Start method findAllParticipantInvestorV1 with page: {} and size: {}", pageable.getPageNumber(),
                pageable.getPageSize());

        Specification<ParticipantInvestorEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (ObjectUtils.isEmpty(participantId)) {
                predicates.add(criteriaBuilder.equal(root.get("participant").get("id"), participantId));
            }
            if (ObjectUtils.isEmpty(investorId)) {
                predicates.add(criteriaBuilder.equal(root.get("investor").get("id"), investorId));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ParticipantInvestorEntity> participantInvestorEntitypage = participantInvestorRepository.findAll(specification, pageable);
        List<ParticipantInvestorOutDto> participantInvestorEntityList = participantInvestorEntitypage.getContent().stream()
                .map(MapperUtil::participantInvestorEntityToParticipantInvestorOutDto)
                .toList();

        log.debug("End method findAllParticipantV1, number of participant found: {}", participantInvestorEntityList.size());
        return MapperPageUtil.pageToPageResponseOutDto(participantInvestorEntityList, participantInvestorEntitypage);
    }

    @Override
    public ParticipantInvestorOutDto createParticipantInvestor(ParticipantInvestorInDto participantInvestorInDto) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return createParticipantInvestorV1(participantInvestorInDto);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ParticipantInvestorOutDto createParticipantInvestorV1(ParticipantInvestorInDto participantInvestorInDto) {
        log.debug("Start method createParticipantInvestorV1, participantInvestor -> {}", participantInvestorInDto);

        ParticipantEntity participantEntity = participantRepository.findById(participantInvestorInDto.getParticipantId())
                .orElseThrow(() -> new ServiceException(ErrorCode.PARTICIPANT_NOT_FOUND, HttpStatus.NOT_FOUND));

        InvestorEntity investorEntity = investorRepository.findById(participantInvestorInDto.getInvestorId())
                .orElseThrow(() -> new ServiceException(ErrorCode.INVESTOR_NOT_FOUND, HttpStatus.NOT_FOUND));

        ParticipantInvestorKey key = ParticipantInvestorKey.builder()
                .investor(participantInvestorInDto.getInvestorId())
                .participant(participantInvestorInDto.getParticipantId())
                .build();
        if (participantInvestorRepository.findById(key).isPresent()) {
            throw new ServiceException(ErrorCode.PARTICIPANT_INVESTOR_EXISTS, HttpStatus.BAD_REQUEST);
        }

        ParticipantInvestorEntity participantInvestorEntity = ParticipantInvestorEntity.builder()
                .participant(participantEntity)
                .investor(investorEntity)
                .lastModifyApplication(propertiesConfig.getApiName())
                .lastModifyIp(PropertiesContext.getClientIp())
                .lastModifyUser(PropertiesContext.getClientUser())
                .lastModifyDate(PropertiesContext.getClientDatetime())
                .build();

        ParticipantInvestorOutDto participantInvestorOutDto = MapperUtil.participantInvestorEntityToParticipantInvestorOutDto(
                participantInvestorRepository.save(participantInvestorEntity));

        log.info("End method createParticipantInvestorV1, participantInvestor -> {}", participantInvestorOutDto);
        return participantInvestorOutDto;
    }

}
