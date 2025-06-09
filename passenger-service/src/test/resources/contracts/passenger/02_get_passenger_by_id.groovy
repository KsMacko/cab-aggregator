package contracts.passenger

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return passenger by ID")
    request {
        method GET()
        urlPath('/api/v1/passengers/1')
    }
    response {
        status OK()
        body([
                profileId: 1,
                firstName: "Иван",
                email: "ivan@example.com",
                phone: "375333333333"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}