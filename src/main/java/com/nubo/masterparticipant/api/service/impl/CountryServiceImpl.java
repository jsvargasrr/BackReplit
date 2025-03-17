package com.nubo.masterparticipant.api.service.impl;

import com.nubo.masterparticipant.api.application.properties.PropertiesConfig;
import com.nubo.masterparticipant.api.application.properties.PropertiesContext;
import com.nubo.masterparticipant.api.exception.ServiceException;
import com.nubo.masterparticipant.api.exception.error.ErrorCode;
import com.nubo.masterparticipant.api.helpers.MapperUtil;
import com.nubo.masterparticipant.api.models.dto.request.CountryInDto;
import com.nubo.masterparticipant.api.models.dto.response.CountryOutDto;
import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import com.nubo.masterparticipant.api.models.entity.CurrencyEntity;
import com.nubo.masterparticipant.api.models.enums.VersionApiEnum;
import com.nubo.masterparticipant.api.repository.CountryRepository;
import com.nubo.masterparticipant.api.repository.CurrencyRepository;
import com.nubo.masterparticipant.api.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;
    private final PropertiesConfig propertiesConfig;

    @Override
    public List<CountryOutDto> findAllCountries() {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findAllCountriesV1();
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private List<CountryOutDto> findAllCountriesV1() {
        log.info("Start method findAllCountriesV1");

        List<CountryOutDto> countryOutDto = countryRepository.findAll().stream()
                .map(MapperUtil::countryEntityToCountryOutDto)
                .toList();

        log.info("End method findAllCountriesV1, number of countries found: {}", countryOutDto.size());

        return countryOutDto;
    }

    @Override
    public CountryOutDto findCountryById(Long countryId) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return findCountryByIdV1(countryId);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private CountryOutDto findCountryByIdV1(Long countryId) {
        log.info("Start method findCountryByIdV1, country with id -> {}", countryId);

        CountryEntity countryEntity = validateCountry(countryId);
        CountryOutDto countryOutDto = MapperUtil.countryEntityToCountryOutDto(countryEntity);

        log.info("End method findCountryByIdV1, country with id: {}", countryOutDto);

        return countryOutDto;
    }

    @Override
    public CountryOutDto updateCountry(Long countryId, CountryInDto countryInDto) {
        if (Objects.requireNonNull(VersionApiEnum.fromValue(PropertiesContext.getVersion())) == VersionApiEnum.V1) {
            return updateCountryV1(countryId, countryInDto);
        } else {
            throw new ServiceException(ErrorCode.VERSION_API_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private CountryOutDto updateCountryV1(Long countryId, CountryInDto countryInDto) {
        log.debug("Start method updateCountryV1, countryId -> {}, country -> {}", countryId, countryInDto);
        CountryEntity countryEntityLast = validateCountry(countryId);

        CountryEntity countryEntity = CountryEntity.builder()
                .id(countryEntityLast.getId())
                .code(ObjectUtils.isEmpty(countryInDto.getCode()) ?
                        countryEntityLast.getCode() : countryInDto.getCode())
                .name(ObjectUtils.isEmpty(countryInDto.getName()) ?
                        countryEntityLast.getName() : countryInDto.getName())
                .currency(ObjectUtils.isEmpty(countryInDto.getCurrencyId()) ?
                        countryEntityLast.getCurrency() : validateCurrency(countryInDto.getCurrencyId()))
                .timeZone(ObjectUtils.isEmpty(countryInDto.getTimeZone()) ?
                        countryEntityLast.getTimeZone() : countryInDto.getTimeZone())
                .lastModifyApplication(propertiesConfig.getApiName())
                .lastModifyIp(PropertiesContext.getClientIp())
                .lastModifyUser(PropertiesContext.getClientUser())
                .lastModifyDate(PropertiesContext.getClientDatetime())
                .build();

        CountryOutDto countryOutDto = MapperUtil.countryEntityToCountryOutDto(countryRepository.save(countryEntity));

        log.info("End method updateCountryV1, country -> {}", countryOutDto);

        return countryOutDto;
    }

    private CountryEntity validateCountry(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ServiceException(ErrorCode.COUNTRY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private CurrencyEntity validateCurrency(Long currencyId) {
        return currencyRepository.findById(currencyId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CURRENCY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

}
