package com.internship.driverservice.unit.service.query;

import com.internship.driverservice.dto.mapper.ProfileMapper;
import com.internship.driverservice.dto.response.ResponseProfileDto;
import com.internship.driverservice.dto.transfer.DriverFilterRequest;
import com.internship.driverservice.dto.transfer.ProfilePackageDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.enums.FieldFilter;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.repo.RateRepo;
import com.internship.driverservice.service.query.ReadDriverProfileService;
import com.internship.driverservice.service.specification.DriverSpecificationService;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import com.internship.driverservice.util.ProfileUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.internship.driverservice.util.ProfileUtil.DEFAULT_PROFILE_ID;
import static com.internship.driverservice.util.ProfileUtil.DEFAULT_RATE;
import static com.internship.driverservice.util.ProfileUtil.DEFAULT_SORT_FIELD;
import static com.internship.driverservice.util.ProfileUtil.driverFilterRequest;
import static com.internship.driverservice.util.ProfileUtil.driverProfile;
import static com.internship.driverservice.util.ProfileUtil.responseProfileDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReadDriverProfileService unit tests")
class ReadDriverProfileServiceTest {

    @Mock
    private DriverProfileRepo driverProfileRepo;

    @Mock
    private RateRepo rateRepo;

    @Mock
    private ProfileMapper profileMapper;

    @Mock
    private DriverSpecificationService specificationService;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @InjectMocks
    private ReadDriverProfileService readDriverProfileService;

    @Test
    @DisplayName("readAllDrivers uses default sort and pageable if not set")
    void readAllDrivers_usesDefaultValuesWhenNull() {
        DriverFilterRequest emptyFilter = new DriverFilterRequest();
        Specification<DriverProfile> spec = mock(Specification.class);
        Pageable expectedPageable = PageRequest.of(
                ProfileUtil.DEFAULT_PAGE,
                ProfileUtil.DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, FieldFilter.valueOf(DEFAULT_SORT_FIELD).getFieldName())
        );
        Page<DriverProfile> resultPage = mockDriverPage(expectedPageable);

        mockCommonDependencies(spec, resultPage);

        ProfilePackageDto dto = readDriverProfileService.readAllDrivers(emptyFilter);

        verify(specificationService).createFilterSpecification(emptyFilter);
        verify(driverProfileRepo).findAll(spec, expectedPageable);
        assertProfilePackageDto(dto, resultPage);
    }

    @Test
    @DisplayName("readAllDrivers returns paginated response with drivers")
    void readAllDrivers_returnsProfilePackageDto() {
        DriverFilterRequest filter = driverFilterRequest();
        Specification<DriverProfile> spec = mock(Specification.class);
        Pageable expectedPageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.Direction.valueOf(filter.getOrder()),
                FieldFilter.valueOf(filter.getSortBy()).getFieldName());
        Page<DriverProfile> resultPage = mockDriverPage(expectedPageable);

        mockCommonDependencies(spec, resultPage);

        ProfilePackageDto dto = readDriverProfileService.readAllDrivers(filter);

        verify(specificationService).createFilterSpecification(filter);
        verify(driverProfileRepo).findAll(spec, expectedPageable);
        assertProfilePackageDto(dto, resultPage);
    }

    private Page<DriverProfile> mockDriverPage(Pageable pageable) {
        List<DriverProfile> profiles = List.of(driverProfile(), driverProfile());
        return new PageImpl<>(profiles, pageable, profiles.size());
    }

    private void mockCommonDependencies(Specification<DriverProfile> spec, Page<DriverProfile> page) {
        when(specificationService.createFilterSpecification(any(DriverFilterRequest.class))).thenReturn(spec);
        when(driverProfileRepo.findAll(eq(spec), any(Pageable.class))).thenReturn(page);
        when(profileMapper.handleEntity(any(DriverProfile.class))).thenReturn(responseProfileDto());
    }
    private void assertProfilePackageDto(ProfilePackageDto dto, Page<DriverProfile> resultPage) {
        assertThat(dto).isNotNull();
        assertThat(dto.profilesDto()).hasSize(resultPage.getContent().size());
        assertThat(dto.totalElements()).isEqualTo(resultPage.getTotalElements());
        assertThat(dto.pageNumber()).isEqualTo(resultPage.getNumber());
        assertThat(dto.pageSize()).isEqualTo(resultPage.getSize());
    }

    @Test
    @DisplayName("readDriverProfileById returns profile with rating")
    void readDriverProfileById_returnsProfileWithRating() {
        Long id = DEFAULT_PROFILE_ID;
        DriverProfile driverProfile = driverProfile();
        ResponseProfileDto responseDto = responseProfileDto();
        Integer rating = DEFAULT_RATE;

        when(profileValidationManager.getDriverProfile(id)).thenReturn(driverProfile);
        when(rateRepo.findDriverRatingByProfileId(id)).thenReturn(rating);
        when(rateRepo.findDriverRatingByProfileId(id)).thenReturn(DEFAULT_RATE);
        when(profileMapper.handleEntity(driverProfile, rating)).thenReturn(responseDto);

        ResponseProfileDto result = readDriverProfileService.readDriverProfileById(id);

        verify(profileValidationManager).getDriverProfile(id);
        verify(rateRepo).findDriverRatingByProfileId(id);

        assertThat(result).isNotNull();
        assertThat(result.rate()).isEqualTo(rating);
        assertThat(result.firstName()).isEqualTo(driverProfile.getFirstName());
    }
}