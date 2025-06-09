package contracts.car

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should set current car by ID")
    request {
        method 'PATCH'
        urlPath('/api/v1/drivers/cars/1')
    }
    response {
        status OK()
        body([
                driverId: 1,
                isCurrent: true,
                carNumber: "1234AA7",
                brand: "TOYOTA",
                color: "Красный"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}