package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantInDto;
import com.nubo.masterparticipant.api.models.dto.request.ParticipantUpdateInDto;
import com.nubo.masterparticipant.api.models.dto.response.PageResponseOutDto;
import com.nubo.masterparticipant.api.models.dto.response.ParticipantOutDto;
import com.nubo.masterparticipant.api.service.ParticipantService;
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

@Tag(name = "Participant")
@RequestMapping("/{version}/participant")
@RestController
@RequiredArgsConstructor
@RateLimited
public class ParticipantRestController {

    private final ParticipantService participantService;

    @Operation(summary = "All Participant", description = "All Participant")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantOutDto[].class)))})
    @GetMapping
    public ResponseEntity<PageResponseOutDto<ParticipantOutDto>> findAllParticipant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(example = "1")
            @RequestParam(required = false) Long statusId) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(participantService.findAllParticipant(pageable, statusId));
    }

    @Operation(summary = "Participant by id", description = "Participant by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantOutDto.class)))})
    @GetMapping("/{participantId}")
    public ResponseEntity<ParticipantOutDto> findParticipantById(@PathVariable Long participantId) {
        return ResponseEntity.ok(participantService.findParticipantById(participantId));
    }

    @Operation(summary = "Get Participant by Trade", description = "Get Participant by trade")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantOutDto.class)))})
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<List<ParticipantOutDto>> getParticipantByTrade(@PathVariable Long tradeId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(participantService.getParticipantByTrade(tradeId, pageable));
    }

    @Operation(summary = "Create participant", description = "Create participant")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantOutDto.class)))})
    @PostMapping
    public ResponseEntity<ParticipantOutDto> createParticipant(@Valid @RequestBody ParticipantInDto participantInDto) {
        ParticipantOutDto participantOutDto = participantService.createParticipant(participantInDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(participantOutDto);
    }

    @Operation(summary = "Update participant", description = "Update participant")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ParticipantOutDto.class)))})
    @PatchMapping("/{participantId}")
    public ResponseEntity<ParticipantOutDto> updateParticipant(@PathVariable Long participantId,
                                                               @Valid @RequestBody ParticipantUpdateInDto participantUpdateInDto) {
        ParticipantOutDto participantOutDto = participantService.updateParticipant(participantId, participantUpdateInDto);
        return ResponseEntity.ok(participantOutDto);
    }


}
