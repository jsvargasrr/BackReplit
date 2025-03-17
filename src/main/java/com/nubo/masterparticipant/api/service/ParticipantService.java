package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.request.ParticipantInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantOutDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParticipantService {

    PageResponseOutDto<ParticipantOutDto> findAllParticipant(Pageable pageable, Long statusId);

    ParticipantOutDto findParticipantById(Long participantId);

    List<ParticipantOutDto> getParticipantByTrade(Long tradeId, Pageable pageable);

    ParticipantOutDto createParticipant(ParticipantInDto participantInDto);

    ParticipantOutDto updateParticipant(Long participantId, ParticipantUpdateInDto participantInDto);

}
