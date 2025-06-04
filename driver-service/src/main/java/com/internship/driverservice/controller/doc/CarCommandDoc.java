package com.internship.driverservice.controller.doc;

import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.dto.response.ResponseCarDto;
import com.internship.driverservice.utils.exceptions.transfer.BaseException;
import com.internship.driverservice.utils.exceptions.transfer.BaseValidationException;
import com.internship.driverservice.utils.validation.ValidationConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Operations for managing cars",
        description = "Endpoints for creating, updating, and deleting cars"
)
public interface CarCommandDoc {
    @Operation(
            summary = "Set a car as the current car for a driver",
            description = "Marks a specific car as the current car for the driver"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Car set as current successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Occurs when the car is not found",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input parameters",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            )
    })
    @PatchMapping("/{id}")
    ResponseEntity<ResponseCarDto> setCurrentCar(
            @Parameter(description = "Unique identifier of the car", example = "1", required = true)
            @PositiveOrZero(message = "id.positive")
            @Max(value = ValidationConstants.MAX_ID_VALUE, message = "id.maxValue")
            @PathVariable Long id);

    @Operation(
            summary = "Add a new car for a driver",
            description = "Adds a new car to the list of cars associated with the driver"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Car added successfully",
                    content = @Content(schema = @Schema(implementation = ResponseCarDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = BaseValidationException.class))
            )
    })
    @PostMapping
    ResponseEntity<ResponseCarDto> addNewCar(
            @Parameter(description = "Car data to add", required = true)
            @Valid
            @RequestBody RequestCarDto carDto);

    @Operation(
            summary = "Delete a car from a driver",
            description = "Deletes a specific car associated with the driver"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Car deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Occurs when the car is not found",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input parameters",
                    content = @Content(schema = @Schema(implementation = BaseException.class))
            )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCar(
            @Parameter(description = "Unique identifier of the car", example = "1", required = true)
            @PositiveOrZero(message = "id.positive")
            @Max(value = ValidationConstants.MAX_ID_VALUE, message = "id.maxValue")
            @PathVariable Long id);
}
