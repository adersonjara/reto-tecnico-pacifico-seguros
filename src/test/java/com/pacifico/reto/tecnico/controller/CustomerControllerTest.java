package com.pacifico.reto.tecnico.controller;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerService service;

    @Test
    void shouldGetCustomerById() {

        CustomerResponse response =
                new CustomerResponse(
                        1L,
                        "Aderson",
                        "Jara",
                        "test@test.com",
                        "999",
                        true
                );

        Mockito.when(
                        service.findById(1L))
                .thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/customers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(1);
    }

    @Test
    void shouldGetAllCustomers() {

        CustomerResponse response =
                new CustomerResponse(
                        1L,
                        "Aderson",
                        "Jara",
                        "test@test.com",
                        "999",
                        true
                );

        Mockito.when(service.findAll())
                .thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/customers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerResponse.class)
                .hasSize(1);
    }

    @Test
    void shouldCreateCustomer() {

        CustomerRequest request =
                new CustomerRequest(
                        "Aderson",
                        "Jara",
                        "test@test.com",
                        "999"
                );

        CustomerResponse response =
                new CustomerResponse(
                        1L,
                        "Aderson",
                        "Jara",
                        "test@test.com",
                        "999",
                        true
                );

        Mockito.when(service.create(request))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/customers")
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}