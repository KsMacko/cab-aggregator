package contracts.fare

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return list of fares")
    request {
        method GET()
        url '/api/v1/fares?order=DESC'
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body([
                fares: [
                        [
                                type: "COMFORT",
                                minPrice: 2.0,
                                freeWaiting: 5,
                                paidWaitingPrice: 1.0,
                                pricePerKm: 0.4,
                                pricePerMin: 0.8]
                ],
                totalCount: 1
        ])

    }
}