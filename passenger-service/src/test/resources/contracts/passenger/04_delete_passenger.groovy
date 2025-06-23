package contracts.passenger

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should delete a passenger by ID")
    request {
        method DELETE()
        urlPath('/api/v1/passengers/1')
    }
    response {
        status NO_CONTENT()
    }
}