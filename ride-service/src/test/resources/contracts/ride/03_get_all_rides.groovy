package contracts.ride

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return list of rides")
    request {
        method GET()
        url '/api/v1/rides?order=DESC'
    }
    response {
        status OK()
        body([
                ridesDto: [
                        [
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
                        ],
                        [
                                id: "def456",
                                driverId: 456,
                                passengerId: 789,
                                promoCode: null,
                                startLocation: "Wall Street",
                                endLocation: ["Brooklyn Bridge"],
                                createdAt: "2025-12-31T23:59:59Z",
                                startWaitingTime: null,
                                startTime: null,
                                endTime: null,
                                distance: 8.0,
                                status: "IN_PROGRESS",
                                fareType: "ECONOMY",
                                price: "12.50",
                                paymentType: "CARD"
                        ]
                ],
                totalElements: 2,
                pageNumber: 0,
                pageSize: 20,
                totalPages: 1
        ])
        headers {
            contentType(applicationJson())
        }
    }
}