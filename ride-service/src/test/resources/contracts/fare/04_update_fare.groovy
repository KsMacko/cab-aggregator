package contracts.fare

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should update an existing fare")
    request {
        method 'PUT'
        url '/api/v1/fares'
        body([
                type: "COMFORT",
                minPrice: 2.0,
                freeWaiting: 10,
                paidWaitingPrice: 1.0,
                pricePerKm: 0.5,
                pricePerMin: 0.7
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                type: "COMFORT",
                minPrice: 2.0,
                freeWaiting: 10,
                paidWaitingPrice: 1.0,
                pricePerKm: 0.5,
                pricePerMin: 0.7
        ])
    }
}