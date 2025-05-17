package com.internship.driverservice.dto.response;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Builder
public record ResponseProfileDto(
        Long profileId,
        String firstName,
        String lastName,
        String fareType,
        String driverStatus,
        String phone,
        Integer rate
) {
}