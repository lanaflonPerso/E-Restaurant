package com.bionic.edu.service;

import com.bionic.edu.entity.Customer;
import com.bionic.edu.util.CustomerBlockedException;

import java.util.List;

public interface CustomerService {

    Customer findById(int id);

    Customer findByEmail(String email);

    List<Customer> findAll();

    void save(Customer customer);

    void delete(int id);


    Customer signIn(String email, String password) throws CustomerBlockedException;
}
