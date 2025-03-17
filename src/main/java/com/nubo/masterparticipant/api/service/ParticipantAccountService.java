package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantAccountOutDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParticipantAccountService {

    PageResponseOutDto<ParticipantAccountOutDto> findAllParticipantAccount(Pageable pageable, Long statusId);

    ParticipantAccountOutDto findParticipantAccountById(Long participantAccountId);

    List<ParticipantAccountOutDto> getParticipantAccountsByTrade(Long tradeId, Pageable pageable);

    ParticipantAccountOutDto createParticipantAccount(ParticipantAccountInDto participantAccountInDto);

    ParticipantAccountOutDto updateParticipantAccount(Long participantAccountId,
                                                      ParticipantAccountUpdateInDto participantAccountUpdateInDto);

}
