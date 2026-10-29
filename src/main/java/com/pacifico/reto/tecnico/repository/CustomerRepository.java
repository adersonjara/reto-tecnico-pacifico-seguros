package com.pacifico.reto.tecnico.repository;

import com.pacifico.reto.tecnico.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Mono<Customer> findByEmail(String email);
}