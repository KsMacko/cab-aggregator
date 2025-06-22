package contracts.ride

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should check participants for a ride")
    request {
        method GET()
        urlPath('/api/v1/rides/ride1/participants')
    }
    response {
        status OK()
        body([
                rideId: "ride1",
                passengerId: 1,
                driverId: 1
        ])
        headers {
            contentType(applicationJson())
        }
    }
}