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
package com.mkdika.spring5restapi.repository;

import com.mkdika.spring5restapi.model.Customer;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Maikel Chandika <mkdika@gmail.com>
 */
@DataJpaTest
@RunWith(SpringRunner.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void checkCustomerCount() {
        assertEquals(5, repository.count());
    }

    @Test
    public void findOne() {
        Optional<Customer> customer = repository.findById(3);
        assertEquals("Benjamin", customer.get().getFirstname());
    }

    @Test
    public void sumAllBalance() {
        List<Customer> cs = (List<Customer>) repository.findAll();
        Double total = cs.stream().mapToDouble(c -> c.getBalance()).sum();
        assertEquals(45500d, total, 0.00001d);
    }

    @Test
    public void exists() {
        assertFalse(repository.existsById(6));
        assertTrue(repository.existsById(2));
    }

    @Test
    public void delete() {
        repository.deleteById(5);
        assertEquals(4, repository.count());
    }

    @Test
    public void deleteAll() {
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @Test
    public void save() {
        repository.save(new Customer(6, "Budi", "Argentum", 1000d));
        entityManager.flush();
        Optional<Customer> cust = repository.findById(6);
        assertEquals("Argentum", cust.get().getLastname());
    }

    @Test
    public void createEntity() {
        Customer c = new Customer();
        c.setId(54);
        c.setFirstname("Budi");
        c.setLastname("ono");
        c.setBalance(666d);
        assertTrue(repository.save(c) != null);                        
    }
}
