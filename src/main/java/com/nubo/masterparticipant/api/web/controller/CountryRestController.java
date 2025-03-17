package com.nubo.masterparticipant.api.web.controller;

import com.nubo.masterparticipant.api.application.annotations.RateLimited;
import com.nubo.masterparticipant.api.models.dto.request.CountryInDto;
import com.nubo.masterparticipant.api.models.dto.response.CountryOutDto;
import com.nubo.masterparticipant.api.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Country")
@RequestMapping("/{version}/country")
@RestController
@RequiredArgsConstructor
@RateLimited
public class CountryRestController {

    private final CountryService countryService;

    @Operation(summary = "Find all countries", description = "Find all countries")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CountryOutDto[].class)))})
    @GetMapping
    public ResponseEntity<List<CountryOutDto>> findAllCountries() {
        List<CountryOutDto> countries = countryService.findAllCountries();
        return ResponseEntity.ok(countries);
    }

    @Operation(summary = "Find country by id", description = "Find country by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CountryOutDto.class)))})
    @GetMapping("/{countryId}")
    public ResponseEntity<CountryOutDto> findCountryById(@PathVariable Long countryId) {
        CountryOutDto countryOutDto = countryService.findCountryById(countryId);
        return ResponseEntity.ok(countryOutDto);
    }

    @Operation(summary = "Update country", description = "Update country")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully Response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CountryOutDto.class)))})
    @PatchMapping("/{countryId}")
    public ResponseEntity<CountryOutDto> updateCountry(@PathVariable Long countryId,
                                                       @Valid @RequestBody CountryInDto countryUpdateInDto) {
        CountryOutDto countryOutDto = countryService.updateCountry(countryId, countryUpdateInDto);
        return ResponseEntity.ok(countryOutDto);
    }

}
