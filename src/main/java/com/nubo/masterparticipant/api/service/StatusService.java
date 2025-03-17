package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.response.StatusOutDto;

import java.util.List;

public interface StatusService {

    List<StatusOutDto> findAllStatus();

}
