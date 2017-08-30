/*
 * The MIT License
 *
 * Copyright 2017 Maikel Chandika <mkdika@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.mkdika.spring5restapi.web.api;

import com.mkdika.spring5restapi.model.Customer;
import com.mkdika.spring5restapi.repository.CustomerRepository;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Maikel Chandika <mkdika@gmail.com>
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository repository;

    @ApiOperation(
            value = "Retrieve all customer data",
            notes = "bla..bla..with note example!",
            response = Customer.class,
            responseContainer = "List",
            produces = "application/json")
    @RequestMapping(method = GET)
    public List<Customer> getCustomers() {
        return (List<Customer>) repository.findAll();
    }

    @ApiOperation(
            value = "Select customer by Id",
            notes = "bla..bla..with note example!",
            response = Customer.class,
            produces = "application/json")
    @RequestMapping(method = GET, value = "/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = repository.findById(id);
        try {
            return customer.get();
        } catch (Exception ex) {
            throw new RuntimeException("Customer not found:" + ex.getLocalizedMessage());
        }
    }

    @ApiOperation(
            value = "Add new customer",
            notes = "bla..bla..with note example!",
            produces = "application/json")
    @RequestMapping(method = POST)
    public void addCustomer(@RequestBody Customer customer) {
        repository.save(customer);
    }

    @ApiOperation(
            value = "Edit customer by Id",
            notes = "bla..bla..with note example!",
            produces = "application/json")
    @RequestMapping(method = PUT, value = "/{id}")
    public void updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        Optional<Customer> c = repository.findById(id);
        c.get().setFirstname(customer.getFirstname());
        c.get().setLastname(customer.getLastname());
        c.get().setBalance(customer.getBalance());
        repository.save(c.get());
    }

    @ApiOperation(
            value = "Delete customer by Id",
            notes = "bla..bla..with note example!",
            produces = "application/json")
    @RequestMapping(method = DELETE, value = "/{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        Optional<Customer> customer = repository.findById(id);        
        try {
            repository.delete(customer.get());
        } catch (Exception ex) {
            throw new RuntimeException("Delete customer failed:" + ex.getLocalizedMessage());
        }
    }
}
