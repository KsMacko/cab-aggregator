package contracts.car

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should add a new car")
    request {
        method 'POST'
        url '/api/v1/drivers/cars'
        body([
                driverId: 1,
                isCurrent: false,
                carNumber: "1234AA7",
                brand: "TOYOTA",
                color: "Красный"
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body([
                driverId: 1,
                isCurrent: false,
                carNumber: "1234AA7",
                brand: "TOYOTA",
                color: "Красный"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}