package com.pacifico.reto.tecnico.service;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.exception.CustomerNotFoundException;
import com.pacifico.reto.tecnico.mapper.CustomerMapper;
import com.pacifico.reto.tecnico.model.Customer;
import com.pacifico.reto.tecnico.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void shouldFindCustomerById() {

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Aderson")
                .lastName("Jara")
                .email("test@test.com")
                .active(true)
                .build();

        when(repository.findById(1L))
                .thenReturn(Mono.just(customer));

        when(mapper.toResponse(customer))
                .thenReturn(new CustomerResponse(
                        1L,
                        "Aderson",
                        "Jara",
                        "test@test.com",
                        null,
                        true
                ));

        StepVerifier.create(service.findById(1L))
                .expectNextCount(1)
                .verifyComplete();

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        when(repository.findById(99L))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                        service.findById(99L))
                .expectError(CustomerNotFoundException.class)
                .verify();
    }

    @Test
    void shouldCreateCustomer() {

        CustomerRequest request =
                new CustomerRequest(
                        "Aderson",
                        "Jara",
                        "test@test.com",
                        "999999999"
                );

        Customer customer = Customer.builder()
                .firstName("Aderson")
                .build();

        when(mapper.toEntity(request))
                .thenReturn(customer);

        when(repository.save(customer))
                .thenReturn(Mono.just(customer));

        when(mapper.toResponse(customer))
                .thenReturn(
                        new CustomerResponse(
                                1L,
                                "Aderson",
                                "Jara",
                                "test@test.com",
                                "999999999",
                                true
                        ));

        StepVerifier.create(
                        service.create(request))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldReturnAllCustomers() {

        Customer customer1 = Customer.builder()
                .id(1L)
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .build();

        when(repository.findAll())
                .thenReturn(
                        Flux.just(customer1, customer2));

        when(mapper.toResponse(any()))
                .thenReturn(
                        new CustomerResponse(
                                1L,
                                "test",
                                "test",
                                "test@test.com",
                                null,
                                true
                        ));

        StepVerifier.create(
                        service.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void shouldDeleteCustomer() {

        Customer customer = Customer.builder()
                .id(1L)
                .build();

        when(repository.findById(1L))
                .thenReturn(Mono.just(customer));

        when(repository.delete(customer))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                        service.delete(1L))
                .verifyComplete();
    }
}