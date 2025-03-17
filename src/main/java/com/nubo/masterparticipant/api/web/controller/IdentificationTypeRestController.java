package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.response.IdentificationTypeOutDto;
import com.nubo.masterparticipant.api.service.IdentificationTypeService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Identification Type")
@RequestMapping("/{version}/identification-type")
@RestController
@RequiredArgsConstructor
@RateLimited
public class IdentificationTypeRestController {

    private final IdentificationTypeService identificationTypeService;

    @Operation(summary = "Find all identification type", description = "Find all identification type")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = IdentificationTypeOutDto[].class)))})
    @GetMapping
    public ResponseEntity<List<IdentificationTypeOutDto>> findAllIdentificationType() {
        return ResponseEntity.ok(identificationTypeService.findAllIdentificationType());
    }

    @Operation(summary = "Find identification type by country", description = "Find identification type by country")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = IdentificationTypeOutDto[].class)))})
    @GetMapping("/{countryId}")
    public ResponseEntity<List<IdentificationTypeOutDto>> findIdentificationTypeByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(identificationTypeService.findIdentificationTypeByCountry(countryId));
    }

}
