package com.internship.financeservice.config

import com.internship.financeservice.service.ArtemisConsumer
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class DisableBroker {

    @Bean
    fun artemisProducer(): ArtemisConsumer = Mockito.mock(ArtemisConsumer::class.java)
}