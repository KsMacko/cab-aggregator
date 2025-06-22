package com.internship.driverservice.controller.query;

import com.internship.driverservice.controller.doc.CarReadDoc;
import com.internship.driverservice.dto.mapper.CarMapper;
import com.internship.driverservice.dto.response.ResponseCarDto;
import com.internship.driverservice.dto.transfer.CarFilterRequest;
import com.internship.driverservice.dto.transfer.CarPackageDto;
import com.internship.driverservice.service.query.ReadCarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class ReadCarController implements CarReadDoc {
    private final ReadCarService carService;
    private final CarMapper carMapper;

    @Override
    public ResponseEntity<ResponseCarDto> getCurrentCar(@PathVariable Long id) {
        return ResponseEntity.ok(carMapper.handleEntity(carService.getCurrentCarByProfileId(id)));
    }

    @Override
    public ResponseEntity<CarPackageDto> getCars(CarFilterRequest filter) {
        return ResponseEntity.ok(carService.readAllCars(filter));
    }
}
