package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.response.TypeOutDto;
import com.nubo.masterparticipant.api.service.TypeService;
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

@Tag(name = "Type")
@RequestMapping("/{version}/type")
@RestController
@RequiredArgsConstructor
@RateLimited
public class TypeRestController {

    private final TypeService typeService;

    @Operation(summary = "Find all type", description = "Find all type")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TypeOutDto[].class)))})
    @GetMapping
    public ResponseEntity<List<TypeOutDto>> findAllType() {
        return ResponseEntity.ok(typeService.findAllType());
    }

}
