package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantInvestorInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantInvestorOutDto;
import com.nubo.masterparticipant.api.service.ParticipantInvestorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Participant Investor")
@RequestMapping("/{version}/participant-investor")
@RestController
@RequiredArgsConstructor
@RateLimited
public class ParticipantInvestorRestController {

    private final ParticipantInvestorService participantInvestorService;

    @Operation(summary = "Find all ParticipantInvestor", description = "Find all ParticipantInvestor")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantInvestorOutDto[].class)))})
    @GetMapping()
    public ResponseEntity<PageResponseOutDto<ParticipantInvestorOutDto>> findAllParticipantInvestor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(example = "1")
            @RequestParam(required = false) Long participantId,
            @Parameter(example = "1")
            @RequestParam(required = false) Long investorId) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(participantInvestorService.findAllParticipantInvestor(pageable, participantId, investorId));
    }

    @Operation(summary = "Create ParticipantInvestor", description = "Create ParticipantInvestor")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantInvestorOutDto.class)))})
    @PostMapping
    public ResponseEntity<ParticipantInvestorOutDto> createParticipantInvestor(
            @Valid @RequestBody ParticipantInvestorInDto participantInvestorInDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participantInvestorService
                .createParticipantInvestor(participantInvestorInDto));
    }

}
