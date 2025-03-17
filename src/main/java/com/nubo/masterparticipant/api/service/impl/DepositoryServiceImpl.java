package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.response.DepositoryOutDto;
import com.nubo.masterparticipant.api.models.entity.DepositoryEntity;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.DepositoryRepository;
import com.nubo.masterparticipant.api.service.DepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositoryServiceImpl implements DepositoryService {

    private final DepositoryRepository depositoryRepository;

    @Override
    public List<DepositoryOutDto> findAllDepository() {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllDepositoryV1();
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<DepositoryOutDto> findAllDepositoryV1() {
        log.info("Start method findAllDepositoryV1");

        List<DepositoryEntity> depositoryEntityList = depositoryRepository.findAll();

        List<DepositoryOutDto> depositoryOutDtoList = depositoryEntityList.stream()
                .map(MapperUtil::depositoryEntityToDepositoryOutDto)
                .toList();

        log.info("End method findAllDepositoryV1, depositoryOutDtoList -> {}", depositoryOutDtoList);

        return depositoryOutDtoList;
    }

}
