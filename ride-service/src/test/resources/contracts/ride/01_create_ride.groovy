package contracts.ride

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should create a new ride")
    request {
        method 'POST'
        url '/api/v1/rides'
        body([
                passengerId: 1,
                startLocation: "Main Street",
                endLocation: ["Park Avenue", "Central Station"],
                distance: 5.6,
                fareType: "COMFORT",
                paymentType: "CASH"
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status CREATED()
        headers {
            contentType(applicationJson())
        }
        body([
                id: anyNonEmptyString(),
                driverId: null,
                passengerId: 1,
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
    }
}