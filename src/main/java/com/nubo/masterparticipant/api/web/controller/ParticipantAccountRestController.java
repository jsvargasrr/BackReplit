package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantAccountUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantAccountOutDto;
import com.nubo.masterparticipant.api.service.ParticipantAccountService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Participant Account")
@RequestMapping("/{version}/participant-account")
@RestController
@RequiredArgsConstructor
@RateLimited
public class ParticipantAccountRestController {

    private final ParticipantAccountService participantAccountService;

    @Operation(summary = "Find all ParticipantAccount", description = "Find all ParticipantAccount")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantAccountOutDto[].class)))})
    @GetMapping
    public ResponseEntity<PageResponseOutDto<ParticipantAccountOutDto>> findAllParticipantAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(example = "1")
            @RequestParam(required = false) Long statusId) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(participantAccountService.findAllParticipantAccount(pageable, statusId));
    }

    @Operation(summary = "Find ParticipantAccount by Id", description = "Find ParticipantAccount by Id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantAccountOutDto.class)))})
    @GetMapping("/{participantAccountId}")
    public ResponseEntity<ParticipantAccountOutDto> findParticipantAccountById(@PathVariable Long participantAccountId) {
        return ResponseEntity.ok(participantAccountService.findParticipantAccountById(participantAccountId));
    }

    @Operation(summary = "Get participant accounts by Trade",
            description = "Retrieve participant accounts associated with a Trade ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantAccountOutDto.class)))})
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<List<ParticipantAccountOutDto>> getParticipantAccountsByTrade(
            @PathVariable Long tradeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(participantAccountService.getParticipantAccountsByTrade(tradeId, pageable));
    }

    @Operation(summary = "Create ParticipantAccount", description = "Create ParticipantAccount")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantAccountOutDto.class)))})
    @PostMapping()
    public ResponseEntity<ParticipantAccountOutDto> createParticipantAccount(
            @Valid @RequestBody ParticipantAccountInDto participantAccountInDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participantAccountService
                .createParticipantAccount(participantAccountInDto));
    }

    @Operation(summary = "Update ParticipantAccount", description = "Update ParticipantAccount")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantAccountOutDto.class)))})
    @PatchMapping("/{participantAccountId}")
    public ResponseEntity<ParticipantAccountOutDto> updateParticipantAccount(
            @PathVariable Long participantAccountId,
            @Valid @RequestBody ParticipantAccountUpdateInDto participantAccountUpdateInDto) {
        return ResponseEntity.ok(participantAccountService
                .updateParticipantAccount(participantAccountId, participantAccountUpdateInDto));
    }

}
