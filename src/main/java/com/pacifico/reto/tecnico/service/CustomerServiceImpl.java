package com.pacifico.reto.tecnico.service;

import com.pacifico.reto.tecnico.dto.CustomerRequest;
import com.pacifico.reto.tecnico.dto.CustomerResponse;
import com.pacifico.reto.tecnico.exception.CustomerNotFoundException;
import com.pacifico.reto.tecnico.mapper.CustomerMapper;
import com.pacifico.reto.tecnico.model.Customer;
import com.pacifico.reto.tecnico.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public Mono<CustomerResponse> create(CustomerRequest request) {

        Customer customer = mapper.toEntity(request);

        return repository.save(customer)
                .map(mapper::toResponse)
                .onErrorResume(ex ->
                        Mono.error(new RuntimeException("Error creating customer", ex))
                );
    }

    @Override
    public Flux<CustomerResponse> findAll() {

        return repository.findAll()
                .map(mapper::toResponse);
    }

    @Override
    public Mono<CustomerResponse> findById(Long id) {

        return repository.findById(id)
                .switchIfEmpty(
                        Mono.error(
                                new CustomerNotFoundException(id)
                        )
                )
                .map(mapper::toResponse);
    }

    @Override
    public Mono<CustomerResponse> update(
            Long id,
            CustomerRequest request) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new CustomerNotFoundException(id)
                        )
                )

                .flatMap(customer -> {

                    customer.setFirstName(
                            Optional.ofNullable(request.firstName())
                                    .orElse(customer.getFirstName())
                    );

                    customer.setLastName(
                            Optional.ofNullable(request.lastName())
                                    .orElse(customer.getLastName())
                    );

                    customer.setEmail(
                            Optional.ofNullable(request.email())
                                    .orElse(customer.getEmail())
                    );

                    customer.setPhone(
                            Optional.ofNullable(request.phone())
                                    .orElse(customer.getPhone())
                    );

                    return repository.save(customer);
                })

                .map(mapper::toResponse);
    }

    @Override
    public Mono<Void> delete(Long id) {

        return repository.findById(id)

                .switchIfEmpty(
                        Mono.error(
                                new CustomerNotFoundException(id)
                        )
                )

                .flatMap(repository::delete);
    }
}