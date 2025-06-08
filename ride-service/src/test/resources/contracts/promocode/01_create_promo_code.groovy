package contracts.promocode

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should create a new promo code")
    request {
        method 'POST'
        url '/api/v1/promo-codes'
        body([
                promoCode: "SUMMER20",
                discount: 20,
                validUntil: "2025-12-31T23:59:59Z"
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
                promoCode: "SUMMER20",
                id: $(anyNonEmptyString()),
                discount: 20,
                validUntil: "2025-12-31T23:59:59Z"
        ])
    }
}