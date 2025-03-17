package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;

import java.util.List;

public interface TypeService {

    List<TypeOutDto> findAllType();

}
