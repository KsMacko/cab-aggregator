package contracts.promocode

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should update an existing promo code")
    request {
        method 'PUT'
        url '/api/v1/promo-codes?promoCodeId=abc123'
        body([
                promoCode: "WINTER10",
                discount: 10,
                validUntil: "2026-01-31T23:59:59Z"
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        body([
                promoCode: "WINTER10",
                id: "abc123",
                discount: 10,
                validUntil: "2026-01-31T23:59:59Z",
                createdAt: "2026-01-31T23:59:59Z"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}