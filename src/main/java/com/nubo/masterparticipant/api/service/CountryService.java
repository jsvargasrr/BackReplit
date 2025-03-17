package com.nubo.masterparticipant.api.service;


import com.nubo.masterparticipant.api.models.dto.request.CountryInDto;
import com.nubo.masterparticipant.api.models.dto.response.CountryOutDto;

import java.util.List;

public interface CountryService {

    List<CountryOutDto> findAllCountries();

    CountryOutDto findCountryById(Long countryId);

    CountryOutDto updateCountry(Long countryId, CountryInDto countryInDto);

}
