package com.internship.passengerservice.controller.doc;

import com.internship.passengerservice.dto.request.RequestRateDto;
import com.internship.passengerservice.dto.response.ResponseRateDto;
import com.internship.passengerservice.utils.exceptions.BaseException;
import com.internship.passengerservice.utils.exceptions.BaseValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.internship.passengerservice.utils.ValidationConstants.MAX_ID_VALUE;

public interface RateDoc {
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Rate set successfully",
                    content = @Content(schema = @Schema(implementation = ResponseRateDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = BaseValidationException.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Occurs when the driver or rate is not found",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            )
    })
    @PostMapping("/author/driver")
    ResponseEntity<ResponseRateDto> setRateToPassenger(
            @Parameter(description = "Rate data to set for the passenger", required = true)
            @Validated
            @RequestBody
            RequestRateDto rateDto);

    @Operation(
            summary = "Delete a rate from a driver",
            description = "Deletes a specific rate associated with a driver"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Rate deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Occurs when the rate is not found",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input parameters",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            )
    })
    @DeleteMapping("/{rateId}/author/driver/{driverId}")
    ResponseEntity<Void> deleteRateFromPassenger(
            @PositiveOrZero(message = "id.positive")
            @Max(value = MAX_ID_VALUE, message = "id.maxValue")
            @PathVariable Long driverId,
            @PositiveOrZero(message = "id.positive")
            @Max(value = MAX_ID_VALUE, message = "id.maxValue")
            @PathVariable Long rateId);
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Rate set successfully",
                    content = @Content(schema = @Schema(implementation = ResponseRateDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = BaseValidationException.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Occurs when the driver or rate is not found",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            )
    })
    @PostMapping("/author/passenger")
    ResponseEntity<ResponseRateDto> setRateToDriver(
            @Parameter(description = "Rate data to set for the driver", required = true)
            @Validated
            @RequestBody
            RequestRateDto rateDto);

    @Operation(
            summary = "Delete a rate from a driver",
            description = "Deletes a specific rate associated with a driver"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Rate deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Occurs when the rate is not found",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input parameters",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            )
    })
    @DeleteMapping("/{rateId}/author/passenger/{passengerId}")
    ResponseEntity<Void> deleteRateFromDriver(
            @PositiveOrZero(message = "id.positive")
            @Max(value = MAX_ID_VALUE, message = "id.maxValue")
            @PathVariable Long passengerId,
            @PositiveOrZero(message = "id.positive")
            @Max(value = MAX_ID_VALUE, message = "id.maxValue")
            @PathVariable Long rateId);
}
