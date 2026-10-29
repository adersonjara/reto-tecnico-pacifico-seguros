package com.pacifico.reto.tecnico.service;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.exception.CustomerNotFoundException;
import com.pacifico.reto.tecnico.mapper.CustomerMapper;
import com.pacifico.reto.tecnico.model.Customer;
import com.pacifico.reto.tecnico.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para CustomerServiceImpl")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    @DisplayName("Debe crear y guardar un cliente exitosamente")
    void testCreateCustomer() {
        // Arrange
        CustomerRequest request =
                new CustomerRequest("Juan", "Pérez", "juan@example.com",
                        "999888777", true);
        Customer customer =
                new Customer(1L, "Juan", "Pérez", "juan@example.com",
                        "999888777",true);
        CustomerResponse response =
                new CustomerResponse(1L, "Juan", "Pérez", "juan@example.com",
                        "999888777", true);

        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        when(customerMapper.toResponse(customer)).thenReturn(response);

        // Act & Assert
        StepVerifier.create(customerService.create(request))
                .expectNext(response)
                .verifyComplete();

        verify(customerMapper).toEntity(request);
        verify(customerRepository).save(customer);
        verify(customerMapper).toResponse(customer);
    }

    @Test
    @DisplayName("Debe retornar una lista de todos los clientes")
    void testFindAllCustomers() {
        // Arrange
        Customer customer =
                new Customer(1L, "Juan", "Pérez", "juan@example.com",
                        "999888777",true);
        CustomerResponse response =
                new CustomerResponse(1L, "Juan", "Pérez", "juan@example.com",
                        "999888777", true);

        when(customerRepository.findAll()).thenReturn(Flux.just(customer));
        when(customerMapper.toResponse(customer)).thenReturn(response);

        // Act & Assert
        StepVerifier.create(customerService.findAll())
                .expectNext(response)
                .verifyComplete();

        verify(customerRepository).findAll();
        verify(customerMapper).toResponse(customer);
    }

    @Test
    @DisplayName("Debe buscar un cliente por ID exitosamente cuando existe")
    void testFindByIdSuccess() {
        // Arrange
        Long id = 1L;
        Customer customer =
                new Customer(id, "Juan", "Pérez", "juan@example.com",
                        "999888777", true);
        CustomerResponse response =
                new CustomerResponse(id, "Juan", "Pérez", "juan@example.com",
                        "999888777", true);

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));
        when(customerMapper.toResponse(customer)).thenReturn(response);

        // Act & Assert
        StepVerifier.create(customerService.findById(id))
                .expectNext(response)
                .verifyComplete();

        verify(customerRepository).findById(id);
        verify(customerMapper).toResponse(customer);
    }

    @Test
    @DisplayName("Debe lanzar CustomerNotFoundException cuando busca por ID y no existe")
    void testFindByIdNotFound() {
        // Arrange
        Long id = 99L;
        when(customerRepository.findById(id)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(customerService.findById(id))
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(customerRepository).findById(id);
    }

    @Test
    @DisplayName("Debe actualizar un cliente existente exitosamente")
    void testUpdateCustomerSuccess() {
        // Arrange
        Long id = 1L;
        CustomerRequest request =
                new CustomerRequest("Carlos", "Gómez", "carlos@example.com",
                        "111222333", true);
        Customer existingCustomer =
                new Customer(id, "Juan", "Pérez", "juan@example.com",
                        "999888777",true);
        Customer updatedCustomer =
                new Customer(id, "Carlos", "Gómez", "carlos@example.com",
                        "111222333",true);
        CustomerResponse response =
                new CustomerResponse(id, "Carlos", "Gómez", "carlos@example.com",
                        "111222333",true);

        when(customerRepository.findById(id)).thenReturn(Mono.just(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(updatedCustomer));
        when(customerMapper.toResponse(updatedCustomer)).thenReturn(response);

        // Act & Assert
        StepVerifier.create(customerService.update(id, request))
                .expectNext(response)
                .verifyComplete();

        verify(customerRepository).findById(id);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Debe lanzar CustomerNotFoundException al intentar actualizar un cliente que no existe")
    void testUpdateCustomerNotFound() {
        // Arrange
        Long id = 99L;
        CustomerRequest request =
                new CustomerRequest("Carlos", "Gómez", "carlos@example.com", "111222333", true);

        when(customerRepository.findById(id)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(customerService.update(id, request))
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(customerRepository).findById(id);
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un cliente exitosamente cuando existe")
    void testDeleteCustomerSuccess() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer(id, "Juan", "Pérez", "juan@example.com", "999888777",true);

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));
        when(customerRepository.delete(customer)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(customerService.delete(id))
                .verifyComplete();

        verify(customerRepository).findById(id);
        verify(customerRepository).delete(customer);
    }

    @Test
    @DisplayName("Debe lanzar CustomerNotFoundException al intentar eliminar un cliente que no existe")
    void testDeleteCustomerNotFound() {
        // Arrange
        Long id = 99L;
        when(customerRepository.findById(id)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(customerService.delete(id))
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(customerRepository).findById(id);
        verify(customerRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Debe lanzar una RuntimeException cuando ocurre un error al crear el cliente")
    void testCreateCustomerError() {
        // Arrange
        CustomerRequest request =
                new CustomerRequest("Juan", "Pérez", "juan@example.com", "999888777", true);
        Customer customer =
                new Customer(1L, "Juan", "Pérez", "juan@example.com", "999888777", true);

        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(customerService.create(request))
                .expectErrorSatisfies(error -> {
                    // Verificamos que se lance el RuntimeException envuelto por el onErrorResume
                    assert error instanceof RuntimeException;
                    assert error.getMessage().contains("Error creating customer");
                })
                .verify();

        verify(customerMapper).toEntity(request);
        verify(customerRepository).save(customer);
    }
}
