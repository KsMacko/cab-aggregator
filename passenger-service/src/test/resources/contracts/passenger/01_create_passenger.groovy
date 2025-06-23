package contracts.passenger

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should create a new passenger profile")
    request {
        method 'POST'
        url '/api/v1/passengers'
        body([
                firstName: "Иван",
                email: "ivan@example.com",
                phone: "375333333333"
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status CREATED()
        body([
                firstName: "Иван",
                email: "ivan@example.com",
                phone: "375333333333"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}