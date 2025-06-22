package com.internship.passengerservice.util;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.passengerservice.dto.request.RequestProfileDto;
import com.internship.passengerservice.dto.response.ResponseProfileDto;
import com.internship.passengerservice.dto.transfer.DataPackageDto;
import com.internship.passengerservice.dto.transfer.ProfileFilterRequest;
import com.internship.passengerservice.entity.PassengerProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileUtil implements UtilConstants{

    public static PassengerProfile validPassengerProfile() {
        return PassengerProfile.builder()
                .profileId(VALID_ID)
                .firstName(VALID_FIRST_NAME)
                .email(VALID_EMAIL)
                .phone(VALID_PHONE)
                .build();
    }

    public static RequestProfileDto validProfileDto() {
        return RequestProfileDto.builder()
                .firstName(VALID_FIRST_NAME)
                .email(VALID_EMAIL)
                .phone(VALID_PHONE)
                .build();
    }

    public static ProfileFilterRequest validProfileFilterRequest() {
        return ProfileFilterRequest.builder()
                .page(DEFAULT_PAGE_VALUE)
                .size(DEFAULT_PAGE_SIZE)
                .sortBy(DEFAULT_FILTER_FIELD)
                .order(DEFAULT_FILTER_ORDER)
                .build();
    }
    public static RideParticipantsConfirmation rideParticipantsConfirmation() {
        return new  RideParticipantsConfirmation(VALID_ID, VALID_ID+1);
    }
    public static ResponseProfileDto responseProfileDto() {
        return ResponseProfileDto.builder()
                .profileId(VALID_ID)
                .firstName(VALID_FIRST_NAME)
                .email(VALID_EMAIL)
                .phone(VALID_PHONE)
                .build();
    }

    public static DataPackageDto dataPackageDto() {
        return DataPackageDto.builder()
                .profiles(List.of(responseProfileDto()))
                .totalElements(1)
                .pageNumber(DEFAULT_PAGE_VALUE)
                .pageSize(1)
                .totalPages(1)
                .build();
    }
}