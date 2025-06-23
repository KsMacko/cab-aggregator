package contracts.car

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return list of cars")
    request {
        method GET()
        url '/api/v1/drivers/cars/cars?driverId=1'
    }
    response {
        status OK()
        body([
                carsDto: [
                        [
                                driverId: 1,
                                isCurrent: true,
                                carNumber: "1234AA7",
                                brand: "TOYOTA",
                                color: "Красный"
                        ]
                ],
                totalElements: 1,
                pageNumber: 0,
                pageSize: 10,
                totalPages: 1
        ])
        headers {
            contentType(applicationJson())
        }
    }
}