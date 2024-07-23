package com.example.demo.controller;

import com.example.demo.entity.Products;
import com.example.demo.repo.OrderDetailRepo;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.ProductRepo;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderCompleteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderFormRepo orderFormRepo;

    @MockBean
    private OrderDetailRepo orderDetailRepo;

    @MockBean
    private ProductRepo productRepo;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testOrderComplete() throws Exception {
        // モックの設定
        Products product = new Products();
        product.setId(1L);
        product.setName("Product 1");

        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(orderFormRepo.save(any())).thenReturn(null);
        when(orderDetailRepo.save(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/orderComplete")
                .param("storeId", "1")
                .param("id", "1")
                .param("name", "Product 1")
                .param("quantities", "2")
                .param("lastName", "Smith")
                .param("totalAmount", "100.0")
                .param("price", "50.0")
                .with(csrf())) // CSRFトークンを含める
                .andExpect(status().isOk())
                .andExpect(view().name("orderComplete"))
                .andExpect(model().attributeExists("cart"));
    }
}
