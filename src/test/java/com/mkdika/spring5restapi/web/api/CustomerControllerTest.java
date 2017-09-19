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

import com.mkdika.spring5restapi.Spring5restapiApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkdika.spring5restapi.model.Customer;
import com.mkdika.spring5restapi.repository.CustomerRepository;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.Charset;
import java.util.Arrays;
import static org.hamcrest.Matchers.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author Maikel Chandika <mkdika@gmail.com>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Spring5restapiApplication.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .build();
        mapper = new ObjectMapper();
    }
    
    @Test
    public void test0RunSpringBoot() {
        
    }

    @Test
    public void test1CheckTotalCustomer() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    public void test2aCheckCustomerById() throws Exception {
        mockMvc.perform(get("/api/customer/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.firstname", is("Benjamin")));
    }

    @Test
    public void test2bCheckCustomerById() throws Exception {
        try {
            mockMvc.perform(get("/api/customer/10"))
                    .andExpect(status().is4xxClientError());
        } catch (Exception ex) {

        }
    }

    @Test
    public void test3CreateCustomer() throws Exception {
        String custJson = json(new Customer(99, "Jack", "Sparrow", 4500d));
        this.mockMvc.perform(post("/api/customer")
                .contentType(contentType)
                .content(custJson))
                .andExpect(status().isOk());
    }

    @Test
    public void test4UpdateCustomer() throws Exception {
        String custJson = json(new Customer(3, "Luben", "Wong", 9990d));
        this.mockMvc.perform(put("/api/customer/")
                .contentType(contentType)
                .content(custJson))
                .andExpect(status().isOk());
    }

    @Test
    public void test5aDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customer/5"))
                .andExpect(status().isOk());
    }

    @Test
    public void test5bDeleteCustomer() throws Exception {
        try {
            mockMvc.perform(delete("/api/customer/2"))
                    .andExpect(status().isOk());
        } catch (Exception ex) {

        }
    }

    protected String json(Customer c) throws IOException {
        return mapper.writeValueAsString(c);
    }
}
