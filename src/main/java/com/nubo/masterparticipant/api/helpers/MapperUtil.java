package com.nubo.masterparticipant.api.helpers;

import com.nubo.masterparticipant.api.models.dto.response.CityOutDto;
import com.nubo.masterparticipant.api.models.dto.response.CountryOutDto;
import com.nubo.masterparticipant.api.models.dto.response.CurrencyOutDto;
import com.nubo.masterparticipant.api.models.dto.response.DepartmentOutDto;
import com.nubo.masterparticipant.api.models.dto.response.DepositoryOutDto;
import com.nubo.masterparticipant.api.models.dto.response.GicsOutDto;
import com.nubo.masterparticipant.api.models.dto.response.IdentificationTypeOutDto;
import com.nubo.masterparticipant.api.models.dto.response.InvestorOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantAccountOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantInvestorOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantOutDto;
import com.nubo.masterparticipant.api.models.dto.response.StatusOutDto;
import com.nubo.masterparticipant.api.models.dto.response.TaxStatusOutDto;
import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;
import com.nubo.masterparticipant.api.models.entity.CityEntity;
import com.nubo.masterparticipant.api.models.entity.CountryEntity;
import com.nubo.masterparticipant.api.models.entity.CurrencyEntity;
import com.nubo.masterparticipant.api.models.entity.DepartmentEntity;
import com.nubo.masterparticipant.api.models.entity.DepositoryEntity;
import com.nubo.masterparticipant.api.models.entity.GicsEntity;
import com.nubo.masterparticipant.api.models.entity.IdentificationTypeEntity;
import com.nubo.masterparticipant.api.models.entity.InvestorEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantAccountEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantEntity;
import com.nubo.masterparticipant.api.models.entity.ParticipantInvestorEntity;
import com.nubo.masterparticipant.api.models.entity.StatusEntity;
import com.nubo.masterparticipant.api.models.entity.TaxStatusEntity;
import com.nubo.masterparticipant.api.models.entity.TypeEntity;
import org.springframework.util.ObjectUtils;

public class MapperUtil {

    private MapperUtil() {
    }

    public static ParticipantOutDto participantEntityToParticipantOutDto(ParticipantEntity participantEntity) {
        return ParticipantOutDto.builder()
                .id(participantEntity.getId())
                .code(participantEntity.getCode())
                .documentNumber(participantEntity.getDocumentNumber())
                .identificationType(ObjectUtils.isEmpty(participantEntity.getIdentificationType()) ?
                        null : identificationTypeEntityToIdentificationTypeOutDto(participantEntity.getIdentificationType()))
                .name(participantEntity.getName())
                .shortName(participantEntity.getShortName())
                .type(ObjectUtils.isEmpty(participantEntity.getType())
                        ? null : typeEntityToTypeOutDto(participantEntity.getType()))
                .mnemonic(participantEntity.getMnemonic())
                .country(ObjectUtils.isEmpty(participantEntity.getCountry())
                        ? null : countryEntityToCountryOutDto(participantEntity.getCountry()))
                .registrationDate(participantEntity.getRegistrationDate())
                .taxCode(participantEntity.getTaxCode())
                .department(ObjectUtils.isEmpty(participantEntity.getDepartment())
                        ? null : departmentEntityToDepartmentOutDto(participantEntity.getDepartment()))
                .mainAddress(participantEntity.getMainAddress())
                .webPage(participantEntity.getWebPage())
                .phoneNumber(participantEntity.getPhoneNumber())
                .status(ObjectUtils.isEmpty(participantEntity.getStatus())
                        ? null : statusEntityToStatusOutDto(participantEntity.getStatus()))
                .nitClearingHouse(participantEntity.getNitClearingHouse())
                .taxStatus(ObjectUtils.isEmpty(participantEntity.getTaxStatus()) ?
                        null : taxStatusEntityToTaxStatusOutDto(participantEntity.getTaxStatus()))
                .typeEntity(participantEntity.getTypeEntity())
                .marginAccount(participantEntity.getMarginAccount())
                .masterAccount(participantEntity.getMasterAccount())
                .directAllocation(participantEntity.getDirectAllocation())
                .marketMaker(participantEntity.getMarketMaker())
                .enabled(participantEntity.getEnabled())
                .custodian(participantEntity.getCustodian())
                .build();
    }

    public static CountryOutDto countryEntityToCountryOutDto(CountryEntity countryEntity) {
        return CountryOutDto.builder()
                .id(countryEntity.getId())
                .code(countryEntity.getCode())
                .name(countryEntity.getName())
                .currency(ObjectUtils.isEmpty(countryEntity.getCurrency())
                        ? null : currencyEntityToCurrencyOutDto(countryEntity.getCurrency()))
                .timeZone(countryEntity.getTimeZone())
                .build();
    }

    public static CurrencyOutDto currencyEntityToCurrencyOutDto(CurrencyEntity currencyEntity) {
        return CurrencyOutDto.builder()
                .id(currencyEntity.getId())
                .code(currencyEntity.getCode())
                .name(currencyEntity.getName())
                .build();
    }

    public static ParticipantAccountOutDto participantAccountEntityToParticipantAccountOutDto(
            ParticipantAccountEntity participantAccountEntity) {
        return ParticipantAccountOutDto.builder()
                .id(participantAccountEntity.getId())
                .participant(ObjectUtils.isEmpty(participantAccountEntity.getParticipant()) ?
                        null : participantEntityToParticipantOutDto(participantAccountEntity.getParticipant()))
                .accountNumber(participantAccountEntity.getAccountNumber())
                .status(ObjectUtils.isEmpty(participantAccountEntity.getStatus())
                        ? null : statusEntityToStatusOutDto(participantAccountEntity.getStatus()))
                .type(ObjectUtils.isEmpty(participantAccountEntity.getType())
                        ? null : typeEntityToTypeOutDto(participantAccountEntity.getType()))
                .build();
    }

    public static ParticipantInvestorOutDto participantInvestorEntityToParticipantInvestorOutDto(
            ParticipantInvestorEntity participantInvestorEntity) {
        return ParticipantInvestorOutDto.builder()
                .participant(participantEntityToParticipantOutDto(participantInvestorEntity.getParticipant()))
                .investor(investorEntityToInvestorOutDto(participantInvestorEntity.getInvestor()))
                .build();
    }

    public static InvestorOutDto investorEntityToInvestorOutDto(InvestorEntity investorEntity) {
        return InvestorOutDto.builder()
                .id(investorEntity.getId())
                .code(investorEntity.getCode())
                .firstName(investorEntity.getFirstName())
                .lastName(investorEntity.getLastName())
                .companyName(investorEntity.getCompanyName())
                .type(ObjectUtils.isEmpty(investorEntity.getType())
                        ? null : typeEntityToTypeOutDto(investorEntity.getType()))
                .status(ObjectUtils.isEmpty(investorEntity.getStatus())
                        ? null : statusEntityToStatusOutDto(investorEntity.getStatus()))
                .country(ObjectUtils.isEmpty(investorEntity.getCountry()) ?
                        null : countryEntityToCountryOutDto(investorEntity.getCountry()))
                .department(ObjectUtils.isEmpty(investorEntity.getDepartment())
                        ? null : departmentEntityToDepartmentOutDto(investorEntity.getDepartment()))
                .city(ObjectUtils.isEmpty(investorEntity.getCity())
                        ? null : cityEntityToCityOutDto(investorEntity.getCity()))
                .documentNumber(investorEntity.getDocumentNumber())
                .documentType(investorEntity.getDocumentType())
                .nationality(investorEntity.getNationality())
                .address(investorEntity.getAddress())
                .phoneNumber(investorEntity.getPhoneNumber())
                .email(investorEntity.getEmail())
                .clearinghouseCode(investorEntity.getClearinghouseCode())
                .gics(ObjectUtils.isEmpty(investorEntity.getGics())
                        ? null : gicsEntityToGicsOutDto(investorEntity.getGics()))
                .moneyLaunderingFlag(investorEntity.getMoneyLaunderingFlag())
                .taxStatus(ObjectUtils.isEmpty(investorEntity.getTaxStatus())
                        ? null : taxStatusEntityToTaxStatusOutDto(investorEntity.getTaxStatus()))
                .build();
    }

    public static StatusOutDto statusEntityToStatusOutDto(StatusEntity statusEntity) {
        return StatusOutDto.builder()
                .id(statusEntity.getId())
                .description(statusEntity.getDescription())
                .category(statusEntity.getCategory())
                .build();
    }

    public static TypeOutDto typeEntityToTypeOutDto(TypeEntity typeEntity) {
        return TypeOutDto.builder()
                .id(typeEntity.getId())
                .description(typeEntity.getDescription())
                .category(typeEntity.getCategory())
                .build();
    }

    public static DepartmentOutDto departmentEntityToDepartmentOutDto(DepartmentEntity departmentEntity) {
        return DepartmentOutDto.builder()
                .id(departmentEntity.getId())
                .description(departmentEntity.getDescription())
                .build();
    }

    public static CityOutDto cityEntityToCityOutDto(CityEntity cityEntity) {
        return CityOutDto.builder()
                .id(cityEntity.getId())
                .description(cityEntity.getDescription())
                .build();
    }

    public static GicsOutDto gicsEntityToGicsOutDto(GicsEntity gicsEntity) {
        return GicsOutDto.builder()
                .id(gicsEntity.getId())
                .description(gicsEntity.getDescription())
                .build();
    }

    public static TaxStatusOutDto taxStatusEntityToTaxStatusOutDto(TaxStatusEntity taxStatusEntity) {
        return TaxStatusOutDto.builder()
                .id(taxStatusEntity.getId())
                .description(taxStatusEntity.getDescription())
                .build();
    }

    public static IdentificationTypeOutDto identificationTypeEntityToIdentificationTypeOutDto(
            IdentificationTypeEntity identificationTypeEntity) {
        return IdentificationTypeOutDto.builder()
                .id(identificationTypeEntity.getId())
                .description(identificationTypeEntity.getDescription())
                .build();
    }

    public static DepositoryOutDto depositoryEntityToDepositoryOutDto(DepositoryEntity depositoryEntity) {
        return DepositoryOutDto.builder()
                .id(depositoryEntity.getId())
                .description(depositoryEntity.getDescription())
                .build();
    }

}
