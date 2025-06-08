package contracts.promocode

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should delete a promo code by ID")
    request {
        method DELETE()
        urlPath('/api/v1/promo-codes/abc123')
    }
    response {
        status NO_CONTENT()
    }
}