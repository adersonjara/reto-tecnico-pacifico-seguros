package com.pacifico.reto.tecnico.mapper;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para CustomerMapper")
class CustomerMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapper();

    @Test
    @DisplayName("Debe convertir CustomerRequest en una entidad Customer correctamente")
    void testToEntity() {
        // Arrange
        CustomerRequest request = new CustomerRequest(
                "Mario",
                "Rossi",
                "mario.rossi@example.com",
                "987654321",
                true
        );

        // Act
        Customer customer = customerMapper.toEntity(request);

        // Assert
        assertNotNull(customer, "La entidad Customer no debe ser nula");
        assertEquals(request.firstName(), customer.getFirstName());
        assertEquals(request.lastName(), customer.getLastName());
        assertEquals(request.email(), customer.getEmail());
        assertEquals(request.phone(), customer.getPhone());
        assertTrue(customer.getActive(), "El cliente mapeado debe estar activo por defecto");
    }

    @Test
    @DisplayName("Debe convertir una entidad Customer en CustomerResponse correctamente")
    void testToResponse() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Mario")
                .lastName("Rossi")
                .email("mario.rossi@example.com")
                .phone("987654321")
                .active(true)
                .build();

        // Act
        CustomerResponse response = customerMapper.toResponse(customer);

        // Assert
        assertNotNull(response, "El DTO CustomerResponse no debe ser nulo");
        assertEquals(customer.getId(), response.id());
        assertEquals(customer.getFirstName(), response.firstName());
        assertEquals(customer.getLastName(), response.lastName());
        assertEquals(customer.getEmail(), response.email());
        assertEquals(customer.getPhone(), response.phone());
        assertEquals(customer.getActive(), response.active());
    }
}
