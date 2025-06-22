package com.internship.financeservice.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MySQLContainer

@TestConfiguration
class MySQLContainerConfig {

    @Bean
    @ServiceConnection
    fun mysqlContainer(): MySQLContainer<*> {
        return MySQLContainer("mysql:8.0")
            .withDatabaseName("test")
            .withUsername("ex")
            .withPassword("ex")
            .withInitScript("sql/create-tables.sql")
    }
}