package com.pacifico.reto.tecnico.util;

import com.pacifico.reto.tecnico.model.Customer;

import java.util.List;

public final class CustomerUtils {

    private CustomerUtils() {
    }

    public static long countActiveCustomers(
            List<Customer> customers) {

        return customers.stream()
                .filter(Customer::getActive)
                .count();
    }
}