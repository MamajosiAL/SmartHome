package com.ucll.smarthome;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("unit-test")
@ExtendWith(SpringExtension.class)
public abstract class AbstractIntegrationTest {

    private static PostgreSQLContainer POSTGRES;
        static {
            POSTGRES = new PostgreSQLContainer("postgres:latest")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("postgres");
            POSTGRES.start();
        }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.username", () -> "postgres");
        dynamicPropertyRegistry.add("spring.datasource.password", () -> "postgres");
        dynamicPropertyRegistry.add("spring.datasource.url",
                () -> String.format(
                        "jdbc:postgresql://localhost:%d/postgres",
                        POSTGRES.getMappedPort(5432)
                )
        );
    }
}
