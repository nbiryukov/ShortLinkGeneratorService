package org.codereview.shortlinkservice;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Базовый класс интеграционных тестов
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ShortLinkApplication.class)
public abstract class IntegrationTestBase {

    /**
     * Контейнер PostgreSQL.
     */
    @ClassRule
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:11.10-alpine")
            .withDatabaseName("short-link-service-test")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        if (!Boolean.parseBoolean(System.getProperty("testcontainers.disabled"))) {
            POSTGRESQL_CONTAINER.start();

            System.setProperty("spring.datasource.url", POSTGRESQL_CONTAINER.getJdbcUrl());
            System.setProperty("spring.datasource.username", POSTGRESQL_CONTAINER.getUsername());
            System.setProperty("spring.datasource.password", POSTGRESQL_CONTAINER.getPassword());
        }
    }

}
