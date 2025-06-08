package contracts.promocode

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return promo code by its code")
    request {
        method GET()
        urlPath('/api/v1/promo-codes/SUMMER20/current')
    }
    response {
        status OK()
        body([
                promoCode: "SUMMER20",
                id: anyNonEmptyString(),
                discount: 20,
                validUntil: "2025-12-31T23:59:59Z"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}