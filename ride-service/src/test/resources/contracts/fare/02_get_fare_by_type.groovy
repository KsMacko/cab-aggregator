package contracts.fare

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return fare by type")
    request {
        method GET()
        urlPath('/api/v1/fares/COMFORT')
    }
    response {
        status OK()
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