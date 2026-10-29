package com.pacifico.reto.tecnico.service;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<CustomerResponse> create(CustomerRequest request);

    Flux<CustomerResponse> findAll();

    Mono<CustomerResponse> findById(Long id);

    Mono<CustomerResponse> update(
            Long id,
            CustomerRequest request);

    Mono<Void> delete(Long id);
}