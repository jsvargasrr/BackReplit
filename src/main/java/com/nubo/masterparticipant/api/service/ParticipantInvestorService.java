package com.nubo.masterparticipant.api.service;

import com.nubo.masterparticipant.api.models.dto.request.ParticipantInvestorInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantInvestorOutDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantInvestorService {

    PageResponseOutDto<ParticipantInvestorOutDto> findAllParticipantInvestor(Pageable pageable, Long participantId, Long investorId);

    ParticipantInvestorOutDto createParticipantInvestor(ParticipantInvestorInDto participantInvestorInDto);
}
