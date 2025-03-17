package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.response.DepositoryOutDto;
import com.nubo.masterparticipant.api.service.DepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Depository")
@RequestMapping("/{version}/depository")
@RestController
@RequiredArgsConstructor
@RateLimited
public class DepositoryRestController {

    private final DepositoryService depositoryService;

    @Operation(summary = "Find all depository", description = "Find all depository")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DepositoryOutDto[].class)))})
    @GetMapping
    public ResponseEntity<List<DepositoryOutDto>> findAllDepository() {
        return ResponseEntity.ok(depositoryService.findAllDepository());
    }

}
