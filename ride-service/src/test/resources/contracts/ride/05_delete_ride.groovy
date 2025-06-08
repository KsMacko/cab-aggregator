package contracts.ride

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should delete a ride by ID")
    request {
        method DELETE()
        urlPath('/api/v1/rides?id=ride1')
    }
    response {
        status NO_CONTENT()
    }
}