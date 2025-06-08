package contracts.promocode

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return list of promo codes")
    request {
        method GET()
        url '/api/v1/promo-codes?sortBy=VALID_UNTIL&order=DESC'
    }
    response {
        status OK()
        body([
                promoCodeDtoList: [
                        [
                                promoCode: "SUMMER20",
                                id: "123",
                                discount: 20,
                                validUntil: "2025-12-31T23:59:59Z",
                                createdAt: "2026-01-31T23:59:59Z"
                        ],
                        [
                                promoCode: "WINTER10",
                                id: "456",
                                discount: 10,
                                validUntil: "2026-01-31T23:59:59Z",
                                createdAt: "2026-01-31T23:59:59Z"
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