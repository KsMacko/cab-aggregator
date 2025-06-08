package contracts.fare

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should create a new fare")
    request {
        method 'POST'
        url '/api/v1/fares'
        body([
                type: "COMFORT",
                minPrice: 2.0,
                freeWaiting: 5,
                paidWaitingPrice: 1.0,
                pricePerKm: 0.4,
                pricePerMin: 0.8
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 201
        headers {
            contentType(applicationJson())
        }
        body([
                type: "COMFORT",
                minPrice: 2.0,
                freeWaiting: 5,
                paidWaitingPrice: 1.0,
                pricePerKm: 0.4,
                pricePerMin: 0.8
        ])

    }
}