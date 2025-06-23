package contracts.passenger

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("Should return list of passengers")
    request {
        method GET()
        url '/api/v1/passengers?sortBy=EMAIL&order=DESC'
    }
    response {
        status OK()
        body([
                profiles: [
                        [
                                profileId: 1,
                                firstName: "Иван",
                                email: "ivan@example.com",
                                phone: "375333333333"
                        ]
                ],
                totalElements: 1,
                pageNumber: 0,
                pageSize: 10,
                totalPages: 1
        ])
        headers {
            contentType(applicationJson())
        }
    }
}