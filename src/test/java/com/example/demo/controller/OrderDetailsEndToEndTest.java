package com.example.demo.controller;

import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.StoreRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.form.OrderForm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderDetailsEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderFormRepo orderFormRepo;

    @MockBean
    private StoreRepo storeRepo;

    @Test
    @WithMockUser(username = "satoutakeru@example.com", roles = "USER")
    public void testOrderHistoryWithStoreId() throws Exception {
        UserData user = new UserData();
        user.setId(1L);
        user.setLastName("佐藤");

        OrderForm order1 = new OrderForm();
        order1.setId(1L);
        order1.setStoreId(1L);
        order1.setTotalAmount(BigDecimal.valueOf(200.0));
        order1.setCreatedAt(LocalDateTime.parse("2024-07-23T10:15:30"));
        order1.setLastName("佐藤");

        OrderForm order2 = new OrderForm();
        order2.setId(2L);
        order2.setStoreId(2L);
        order2.setTotalAmount(BigDecimal.valueOf(300.0));
        order2.setCreatedAt(LocalDateTime.parse("2024-07-24T11:20:45"));
        order2.setLastName("佐藤");

        StoreData store1 = new StoreData();
        store1.setId(1L);
        store1.setName("Store 1");

        StoreData store2 = new StoreData();
        store2.setId(2L);
        store2.setName("Store 2");

        when(orderFormRepo.findByStoreId(anyLong())).thenReturn(List.of(order1, order2));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store1));
        when(storeRepo.findById(2L)).thenReturn(Optional.of(store2));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/order_history")
                .param("storeId", "1")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.lastName").value("佐藤"))
            .andExpect(jsonPath("$.orders[0].orderId").value(1))
            .andExpect(jsonPath("$.orders[0].storeName").value("Store 1"))
            .andExpect(jsonPath("$.orders[0].totalAmount").value(200.0))
            .andExpect(jsonPath("$.orders[0].lastName").value("佐藤"))
            .andExpect(jsonPath("$.orders[1].orderId").value(2))
            .andExpect(jsonPath("$.orders[1].storeName").value("Store 2"))
            .andExpect(jsonPath("$.orders[1].totalAmount").value(300.0))
            .andExpect(jsonPath("$.orders[1].lastName").value("佐藤"));
    }

    @Test
    @WithMockUser(username = "satoutakeru@example.com", roles = "USER")
    public void testOrderHistoryWithoutStoreId() throws Exception {
        UserData user = new UserData();
        user.setId(1L);
        user.setLastName("佐藤");

        OrderForm order1 = new OrderForm();
        order1.setId(1L);
        order1.setStoreId(1L);
        order1.setTotalAmount(BigDecimal.valueOf(200.0));
        order1.setLastName("佐藤");

        OrderForm order2 = new OrderForm();
        order2.setId(2L);
        order2.setStoreId(2L);
        order2.setTotalAmount(BigDecimal.valueOf(300.0));
        order2.setLastName("佐藤");

        StoreData store1 = new StoreData();
        store1.setId(1L);
        store1.setName("Store 1");

        StoreData store2 = new StoreData();
        store2.setId(2L);
        store2.setName("Store 2");

        when(orderFormRepo.findAll()).thenReturn(List.of(order1, order2));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store1));
        when(storeRepo.findById(2L)).thenReturn(Optional.of(store2));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/order_history")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.lastName").value("佐藤"))
            .andExpect(jsonPath("$.orders[0].orderId").value(1))
            .andExpect(jsonPath("$.orders[0].storeName").value("Store 1"))
            .andExpect(jsonPath("$.orders[0].totalAmount").value(200.0))
            .andExpect(jsonPath("$.orders[0].lastName").value("佐藤"))
            .andExpect(jsonPath("$.orders[1].orderId").value(2))
            .andExpect(jsonPath("$.orders[1].storeName").value("Store 2"))
            .andExpect(jsonPath("$.orders[1].totalAmount").value(300.0))
            .andExpect(jsonPath("$.orders[1].lastName").value("佐藤"));
    }

    @Test
    @WithMockUser(username = "satoutakeru@example.com", roles = "USER")
    public void testOrderHistoryPage() throws Exception {
        UserData user = new UserData();
        user.setId(1L);
        user.setLastName("佐藤");

        OrderForm order1 = new OrderForm();
        order1.setId(1L);
        order1.setStoreId(1L);
        order1.setTotalAmount(BigDecimal.valueOf(200.0));
        order1.setLastName("佐藤");

        OrderForm order2 = new OrderForm();
        order2.setId(2L);
        order2.setStoreId(2L);
        order2.setTotalAmount(BigDecimal.valueOf(300.0));
        order2.setLastName("佐藤");

        StoreData store1 = new StoreData();
        store1.setId(1L);
        store1.setName("Store 1");

        StoreData store2 = new StoreData();
        store2.setId(2L);
        store2.setName("Store 2");

        when(orderFormRepo.findAll()).thenReturn(List.of(order1, order2));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store1));
        when(storeRepo.findById(2L)).thenReturn(Optional.of(store2));
        when(storeRepo.findAll()).thenReturn(List.of(store1, store2));

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/order_history_page")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("order_history"))
            .andExpect(model().attributeExists("orders"))
            .andExpect(model().attributeExists("stores"))
            .andExpect(model().attribute("orders", List.of(order1, order2)))
            .andExpect(model().attribute("stores", List.of(store1, store2)));
    }

    @Test
    @WithMockUser(username = "satoutakeru@example.com", roles = "USER")
    public void testOrderDetail() throws Exception {
        OrderForm order = new OrderForm();
        order.setId(1L);
        order.setStoreId(1L);
        order.setTotalAmount(BigDecimal.valueOf(200.0));
        order.setLastName("佐藤");

        StoreData store = new StoreData();
        store.setId(1L);
        store.setName("Store 1");

        when(orderFormRepo.findById(anyLong())).thenReturn(Optional.of(order));
        when(storeRepo.findById(anyLong())).thenReturn(Optional.of(store));

        mockMvc.perform(get("/order_history_detail/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(view().name("order_history_detail"))
            .andExpect(model().attributeExists("orderDetail"))
            .andExpect(model().attribute("orderDetail", order));
    }
}
