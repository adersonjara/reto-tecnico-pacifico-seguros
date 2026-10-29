package com.pacifico.reto.tecnico.controller;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para CustomerController")
class CustomerControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(customerController).build();
    }

    @Test
    @DisplayName("Debe crear un cliente correctamente y retornar estado HTTP 201 (Created)")
    void testCreateCustomer() {
        // Arrange
        CustomerRequest request =
                new CustomerRequest("Juan", "Pérez", "juan.perez@example.com",
                        "999888777", true);
        CustomerResponse response =
                new CustomerResponse(1L, "Juan", "Pérez", "juan.perez@example.com",
                        "999888777", true);

        when(customerService.create(any(CustomerRequest.class))).thenReturn(Mono.just(response));

        // Act & Assert
        webTestClient.post()
                .uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerResponse.class);
    }

    @Test
    @DisplayName("Debe listar todos los clientes y retornar estado HTTP 200 (OK)")
    void testFindAllCustomers() {
        // Arrange
        CustomerResponse response =
                new CustomerResponse(1L, "Juan", "Pérez", "juan.perez@example.com",
                        "999888777",true);
        when(customerService.findAll()).thenReturn(Flux.just(response));

        // Act & Assert
        webTestClient.get()
                .uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponse.class);
    }

    @Test
    @DisplayName("Debe buscar un cliente por ID y retornar estado HTTP 200 (OK)")
    void testFindByIdCustomer() {
        // Arrange
        Long id = 1L;
        CustomerResponse response =
                new CustomerResponse(id, "Juan", "Pérez", "juan.perez@example.com",
                        "999888777",true);
        when(customerService.findById(id)).thenReturn(Mono.just(response));

        // Act & Assert
        webTestClient.get()
                .uri("/api/customers/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponse.class);
    }

    @Test
    @DisplayName("Debe actualizar un cliente existente y retornar estado HTTP 200 (OK)")
    void testUpdateCustomer() {
        // Arrange
        Long id = 1L;
        CustomerRequest request =
                new CustomerRequest("Juan", "Pérez", "juan.perez@example.com",
                        "999888777", true);
        CustomerResponse response =
                new CustomerResponse(id,"Juan", "Pérez", "juan.perez@example.com",
                        "999888777",true);

        when(customerService.update(eq(id), any(CustomerRequest.class))).thenReturn(Mono.just(response));

        // Act & Assert
        webTestClient.put()
                .uri("/api/customers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerResponse.class);
    }

    @Test
    @DisplayName("Debe eliminar un cliente y retornar estado HTTP 204 (No Content)")
    void testDeleteCustomer() {
        // Arrange
        Long id = 1L;
        when(customerService.delete(id)).thenReturn(Mono.empty());

        // Act & Assert
        webTestClient.delete()
                .uri("/api/customers/{id}", id)
                .exchange()
                .expectStatus().isNoContent();
    }
}
