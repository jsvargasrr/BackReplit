package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.TypeRepository;
import com.nubo.masterparticipant.api.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;

    @Override
    public List<TypeOutDto> findAllType() {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllTypeV1();
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<TypeOutDto> findAllTypeV1() {
        log.info("Start method findAllTypeV1");

        List<TypeEntity> typeEntityList = typeRepository.findAll();

        List<TypeOutDto> typeOutDtoList = typeEntityList.stream()
                .map(MapperUtil::typeEntityToTypeOutDto)
                .toList();

        log.info("End method findAllTypeV1, typeOutDtoList -> {}", typeOutDtoList);

        return typeOutDtoList;
    }

}
