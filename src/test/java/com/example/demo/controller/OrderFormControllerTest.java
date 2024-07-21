package com.example.demo.controller;

import com.example.demo.repo.OrderDetailRepo;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
public class OrderFormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderFormRepo orderFormRepo;

    @MockBean
    private OrderDetailRepo orderDetailRepo;

    @MockBean
    private ProductRepo productRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    public void testOrderFormNotLoggedIn() throws Exception {
        mockMvc.perform(get("/orderForm"))
                .andExpect(status().isUnauthorized());
    }
}
