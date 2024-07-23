package com.example.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import com.example.demo.entity.StoreData;
import com.example.demo.form.OrderForm;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.StoreRepo;

import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(OrderHistoryController.class)
public class OrderHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderFormRepo orderFormRepo;

    @MockBean
    private StoreRepo storeRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testOrderHistoryPage() throws Exception {
        StoreData store1 = new StoreData();
        store1.setId(1L);
        store1.setName("Store 1");

        StoreData store2 = new StoreData();
        store2.setId(2L);
        store2.setName("Store 2");

        OrderForm order1 = new OrderForm();
        order1.setId(1L);
        order1.setStoreId(1L);

        OrderForm order2 = new OrderForm();
        order2.setId(2L);
        order2.setStoreId(2L);

        when(storeRepo.findAll()).thenReturn(Arrays.asList(store1, store2));
        when(orderFormRepo.findAll()).thenReturn(Arrays.asList(order1, order2));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store1));
        when(storeRepo.findById(2L)).thenReturn(Optional.of(store2));

        mockMvc.perform(MockMvcRequestBuilders.get("/order_history_page"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_history"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("stores"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testOrderDetail() throws Exception {
        OrderForm order = new OrderForm();
        order.setId(1L);
        order.setStoreId(1L);

        StoreData store = new StoreData();
        store.setId(1L);
        store.setName("Store 1");

        when(orderFormRepo.findById(1L)).thenReturn(Optional.of(order));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store));

        mockMvc.perform(MockMvcRequestBuilders.get("/order_history_detail/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_history_detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderDetail"));
    }
}
