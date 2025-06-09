package contracts.car

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should add a new driver")
    request {
        method 'POST'
        url '/api/v1/drivers'
        body([
                firstName: "Иван",
                lastName: "Петров",
                fareType: "COMFORT",
                phone: "375291234567"
        ])
        headers {
            contentType(applicationJson())
        }
    }
    response {

        status CREATED()
        body([
                firstName: "Иван",
                lastName: "Петров",
                fareType: "COMFORT",
                phone: "375291234567"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}