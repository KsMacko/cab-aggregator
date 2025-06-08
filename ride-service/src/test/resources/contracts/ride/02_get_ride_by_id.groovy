package contracts.ride

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return ride by its ID")
    request {
        method GET()
        urlPath('/api/v1/rides/ride1?order=DESC')
    }
    response {
        status OK()
        body([
                id: "ride1",
                driverId: null,
                passengerId: 123,
                promoCode: "SUMMER20",
                startLocation: "Main Street",
                endLocation: ["Park Avenue", "Central Station"],
                createdAt: "2025-12-31T23:59:59Z",
                startWaitingTime: null,
                startTime: null,
                endTime: null,
                distance: 5.6,
                status: "CREATED",
                fareType: "COMFORT",
                price: "0.00",
                paymentType: "CASH"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}