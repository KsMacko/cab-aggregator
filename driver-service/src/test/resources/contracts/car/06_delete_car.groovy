package contracts.car

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should delete a car by ID")
    request {
        method DELETE()
        urlPath('/api/v1/drivers/cars/1')
    }
    response {
        status NO_CONTENT()
    }
}