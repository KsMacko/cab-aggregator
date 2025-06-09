package contracts.car

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return current car for driver")
    request {
        method GET()
        urlPath('/api/v1/drivers/cars/1/cars/current')
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