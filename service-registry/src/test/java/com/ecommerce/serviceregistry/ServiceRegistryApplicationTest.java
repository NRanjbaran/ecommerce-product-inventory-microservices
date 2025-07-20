package com.ecommerce.serviceregistry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the ServiceRegistryApplication.
 * This class ensures that the Eureka Server context loads successfully
 * and that its key endpoints are accessible, indicating the registry is operational.
 * <p>
 * It uses a random port to avoid conflicts when running tests in parallel
 * or on systems where the default port might be in use.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // to avoid conflicts
class ServiceRegistryApplicationTest {

    @Value(value = "${local.server.port}")
    private int port;

    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void init() {
        testRestTemplate = new TestRestTemplate();
    }

    /**
     * Test case to verify that the Spring application context loads successfully.
     * This is a fundamental check to ensure there are no configuration errors
     * or missing dependencies that prevent the application from starting.
     * <p>
     * Asserting true is a basic way to confirm the context loaded without throwing an exception.
     */
    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }

    /**
     * Test case to verify that the Eureka Dashboard (root URL) is accessible.
     * This checks if the web interface of the Eureka server is up or not.
     */
    @Test
    void eurekaDashboardAccessible() {
        String url = "http://localhost:" + port + "/";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Instances currently registered with Eureka");
    }

    /**
     * Test case to verify that the /eureka/apps endpoint is accessible.
     * This endpoint is used by Eureka clients to fetch the list of registered applications.
     * Accessing it confirms a core programmatic function of the Eureka server.
     */
    @Test
    void eurekaAppsEndpointAccessible() {
        String url = "http://localhost:" + port + "/eureka/apps";
        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("applications");
    }
}