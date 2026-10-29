package com.pacifico.reto.tecnico.controller;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponse> create(
            @Valid @RequestBody CustomerRequest request) {

        return service.create(request);
    }

    @GetMapping
    public Flux<CustomerResponse> findAll() {

        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CustomerResponse> findById(
            @PathVariable Long id) {

        return service.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(
            @PathVariable Long id) {

        return service.delete(id);
    }
}
