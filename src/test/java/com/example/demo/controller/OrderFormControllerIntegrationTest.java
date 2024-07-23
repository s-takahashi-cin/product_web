package com.example.demo.controller;

import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Products;
import com.example.demo.entity.UserData;
import com.example.demo.form.OrderForm;
import com.example.demo.repo.OrderDetailRepo;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderFormControllerIntegrationTest {

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
    @WithMockUser
    public void testOrderForm() throws Exception {
        UserData user = new UserData();
        user.setId(1L);

        List<OrderDetails> cart = new ArrayList<>();
        mockMvc.perform(get("/orderForm")
                .sessionAttr("cart", cart)
                .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    @WithMockUser
    public void testOrderComplete() throws Exception {
        when(productRepo.findById(any(Long.class))).thenReturn(Optional.of(new Products()));
        when(orderFormRepo.save(any())).thenReturn(new OrderForm());
        when(orderDetailRepo.save(any())).thenReturn(new OrderDetails());

        mockMvc.perform(post("/orderComplete")
                .param("storeId", "1")
                .param("id", "1")
                .param("name", "Product")
                .param("quantities", "1")
                .param("lastName", "Doe")
                .param("totalAmount", "100.00")
                .param("price", "10.00")
                .sessionAttr("cart", new ArrayList<>()))
                .andExpect(status().isOk())
                .andExpect(view().name("orderComplete"));
    }
}

