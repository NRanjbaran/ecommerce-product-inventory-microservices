package com.ecommerce.configserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the ConfigServerApplication.
 * This class verifies that the Spring Cloud Config Server context loads correctly
 * and that it can successfully serve configuration files for various applications
 * from the configured repository (config-repo).
 * <p>
 * It uses a random port to avoid conflicts and ensure test isolation.
 * Note: These tests assume that the 'config-repo' directory is set up correctly
 * and contains the respective application.yml files (product-service.yml, inventory-service.yml, api-gateway.yml).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // to avoid conflicts
class ConfigServerApplicationTest {

    @Value(value = "${local.server.port}")
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    /*@BeforeEach
    void init() {
        restTemplate = new TestRestTemplate();
    }
*/
    /**
     * Test case to verify that the Spring application context loads successfully.
     * This is a fundamental check for overall application health.
     */
    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }

    /**
     * Test case to verify that the Config Server's basic health endpoint is accessible.
     * This confirms that the server is up and responsive.
     */
    @Test
    void configServiceAccessible() {
        String url = "http://localhost:" + port + "/actuator/health";
        ResponseEntity<String> healthResponse = restTemplate.getForEntity(url, String.class);

        assertThat(healthResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(healthResponse.getBody()).contains("UP");
    }

    /**
     * Test case to verify that the configuration for 'product-service' can be successfully loaded.
     * It checks if the Config Server correctly retrieves and serves the 'product-service.yml'
     * from the configured 'config-repo'.
     */
    @Test
    void productConfigLoads() {
        String url = "http://localhost:" + port + "/product-service/default";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("product-service");
    }

    /**
     * Test case to verify that the configuration for 'inventory-service' can be successfully loaded.
     * Similar to productConfigLoads, but for the inventory service.
     */
    @Test
    void inventoryConfigLoads() {
        String url = "http://localhost:" + port + "/inventory-service/default";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("inventory-service");
    }

    /**
     * Test case to verify that the configuration for 'api-gateway' can be successfully loaded.
     * This checks if the Config Server can serve the gateway's routing and filter configurations.
     */
    @Test
    void apiGatewayConfigLoads() {
        String url = "http://localhost:" + port + "/api-gateway/default";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("api-gateway");
    }
}