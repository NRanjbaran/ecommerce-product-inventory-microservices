# e-commerce-product-inventory-microservices
This sample project demonstrates a microservices architecture for a simplified e-commerce application, showcasing key Spring Cloud features for service discovery, centralized configuration, API Gateway and inter-service communication using both REST and gRPC. It includes a basic React frontend to interact with the backend services.

## Features

* **Service Discovery:** Microservices register with and discover each other via Eureka.
* **Centralized Configuration:** All service configurations are managed externally by a Spring Cloud Config Server, pulling from a local Git repository (`config-repo`).
* **API Gateway:** A single entry point for external clients (React UI), handling routing and load balancing.
* **Polyglot Communication:**
    * **REST:** Used for external communication (React UI to API Gateway to Product Service).
    * **gRPC:** Used for high-performance, strongly-typed internal communication (Product Service to Inventory Service).
* **Database per Service:** Each microservice (`ProductService`, `InventoryService`) owns its dedicated PostgreSQL database.
* **Spring Data JPA:** For robust data persistence in both services.
* **React Frontend:** A simple web interface for Browse products and simulating inventory updates.

## Architecture

* **Eureka Server (`service-registry`):** The service registry.
* **Config Server (`config-server`):** Provides externalized configuration to all Spring Boot services.
* **API Gateway (`api-gateway`):** Routes external requests to the appropriate microservices.
* **Product Service (`product-service`):** Manages product details (name, description, price) and interacts with the `InventoryService` via gRPC to fetch/update stock. Exposes REST endpoints to the Gateway.
* **Inventory Service (`inventory-service`):** Manages product stock quantities and exposes a gRPC API for `ProductService`.
* **PostgreSQL Databases:** Two separate instances (managed by a single `postgres` container with multiple databases) for `product_db` and `inventory_db`.
* **React Frontend (`frontend-react-app`):** A client-side application that interacts with the `API Gateway`.

<img src="/doc/image/Arch.png"/>

## Technologies Used

* **Backend:**
  * Java 21
  * Spring Boot 3.5.3
  * Spring Cloud:
    * Spring Cloud Netflix Eureka (for Service Discovery)
    * Spring Cloud Config (for Centralized Configuration)
    * Spring Cloud Gateway (for API Gateway)
  * gRPC
  * PostgreSQL
  * Spring Data JPA
  * Gradle
* **Frontend:**
  * React 19.1
  * Axios
* **Containerization:**
  * Docker
  * Docker Compose

## Getting Started

Follow these instructions to set up and run the project locally.

### Prerequisites

* Java Development Kit (JDK) 21
* Node.js (LTS version, includes npm)
* Docker Desktop (or Docker Engine and Docker Compose)
* Git

### Setup and Running

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/NRanjbaran/ecommerce-product-inventory-microservices.git](https://github.com/nranjbaran/ecommerce-product-inventory-microservices.git)
    cd ecommerce-microservices
    ```

2.  **Initialize Config Repository:**
    The `config-server` reads configurations from my Git repository. For local development, this project uses a local Git repository within the `config-repo` directory.
    ```bash
    cd config-repo
    git init
    git add .
    git commit -m "Initial configuration files"
    cd .. # Go back to the root directory
    ```
    *Note: In a production or more complex setup, `config-repo` would typically be a separate, remote Git repository.*

3.  **Build Spring Boot Applications:**
    Navigate to the root of the project and build all the Spring Boot services using Gradle. This step is crucial as it also generates the necessary gRPC stubs from the `.proto` files.
    ```bash
    ./gradlew clean build -x test
    ```
    
4.  **Build Docker Images:**
    Build Docker images for each Spring Boot microservice defined in `docker-compose.yml`. Ensure Docker Desktop is running.
    ```bash
    docker compose build
    ```

5.  **Start All Services with Docker Compose:**
    This command will start all PostgreSQL databases, Eureka Server, Config Server, API Gateway, Product Service, and Inventory Service as containers.
    ```bash
    docker compose up
    ```
    Wait for all services to fully start. You can monitor their logs in the terminal.

6.  **Start React Frontend:**
    In a new terminal, navigate to the `frontend-react-app` directory and start the React development server:
    ```bash
    cd frontend-react-app
    npm install 
    npm start
    ```
    Open the React application at `http://localhost:3000`.

### Accessing Applications

* **React Frontend:** `http://localhost:3000`
* **API Gateway:** `http://localhost:8080` (All UI requests go through here)
* **Eureka Dashboard:** `http://localhost:8761` (Monitor registered services and their health)
* **Config Server:** `http://localhost:8888` (You can verify configurations, `http://localhost:8888/product-service/default`)

## API Endpoints

The React UI interacts with the following REST endpoints exposed by the **API Gateway**:

* **GET `/products`**: Retrieve a list of all products with their current stock.
  * Example Request: `GET http://localhost:8080/products`
  * Example Response:
      ```json
      [
          {
              "id": 1,
              "name": "Laptop Pro",
              "description": "High-performance laptop",
              "price": 1200.00,
              "stockQuantity": 50
          },
          {
              "id": 2,
              "name": "Mechanical Keyboard",
              "description": "Clicky and durable",
              "price": 85.50,
              "stockQuantity": 200
          }
      ]
      ```
* **PUT `/products/{productId}/stock`**: Update the stock quantity for a specific product.
  * Example Request: `PUT http://localhost:8080/products/1/stock`
  * Request Body (JSON):
      ```json
      {
          "quantity": 100
      }
      ```
  * Example Response: HTTP 200 OK (or appropriate error status)

## Flows Illustrated

This project demonstrates the following key interaction flows:

1.  **Service Registration and Discovery:**
  * Upon startup, `product-service`, `inventory-service`, and `api-gateway` register themselves with the `eureka-server`.
  * The `api-gateway` uses Eureka to dynamically discover and route requests to available instances of `product-service`.

2.  **Centralized Configuration:**
  * Each Spring Boot microservice retrieves its specific configurations (e.g., database connection details, Eureka server URL, gRPC port) from the `config-server` on startup.

3.  **External REST Communication (UI to Product Service via Gateway):**
  * The React UI makes REST calls (e.g., `GET /products`, `PUT /products/{productId}/stock`) to the `API Gateway`.
  * The `API Gateway` routes these requests to an available `ProductService` instance, providing client-side load balancing.

4.  **Internal gRPC Communication (Product Service to Inventory Service):**
  * When the `ProductService` receives a request (e.g., `GET /products` or `PUT /products/{productId}/stock`), it makes internal gRPC calls to the `InventoryService` to fetch or update the real-time stock quantity for the respective product.
