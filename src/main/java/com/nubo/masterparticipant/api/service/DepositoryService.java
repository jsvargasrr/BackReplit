package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.response.DepositoryOutDto;

import java.util.List;

public interface DepositoryService {

    List<DepositoryOutDto> findAllDepository();

}
