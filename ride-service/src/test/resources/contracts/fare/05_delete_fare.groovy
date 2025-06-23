package contracts.fare

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should delete a fare by type")
    request {
        method 'DELETE'
        urlPath('/api/v1/fares/COMFORT')
    }
    response {
        status 204
    }
}