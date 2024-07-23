package com.example.demo.controller;

import com.example.demo.form.OrderForm;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.StoreRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = {OrderHistoryController.ShowOrderHistoryController.class, OrderHistoryController.class})
@ActiveProfiles("test")
public class OrderHistoryControllerIntegrationTest {

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
    @WithMockUser
    public void testOrderHistory() throws Exception {
        UserData user = new UserData();
        user.setId(1L);
        user.setLastName("Doe");

        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant()
                                          .atZone(ZoneId.systemDefault())
                                          .toLocalDateTime();  
    
        OrderForm order1 = new OrderForm();
        order1.setId(1L);
        order1.setStoreId(1L);
        order1.setTotalAmount(BigDecimal.valueOf(100.00)); // doubleをBigDecimalに変換
        order1.setCreatedAt(localDateTime);
        order1.setLastName("Doe");
    
        StoreData store = new StoreData();
        store.setId(1L);
        store.setName("Store1");
    
        when(orderFormRepo.findAll()).thenReturn(Collections.singletonList(order1));
        when(orderFormRepo.findByStoreId(1L)).thenReturn(Collections.singletonList(order1));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/order_history")
                .param("storeId", "1")
                .sessionAttr("user", user)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders[0].orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders[0].storeName").value(store.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orders[0].totalAmount").value(order1.getTotalAmount()));
    }

    @Test
    @WithMockUser
    public void testOrderHistoryPage() throws Exception {
        OrderForm order1 = new OrderForm();
        order1.setId(1L);
        order1.setStoreId(1L);

        StoreData store = new StoreData();
        store.setId(1L);
        store.setName("Store1");

        when(orderFormRepo.findAll()).thenReturn(Collections.singletonList(order1));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(store));
        when(storeRepo.findAll()).thenReturn(Collections.singletonList(store));

        mockMvc.perform(get("/order_history_page"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_history"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("stores"));
    }

    @Test
    @WithMockUser
    public void testOrderDetail() throws Exception {
        OrderForm order = new OrderForm();
        order.setId(1L);

        when(orderFormRepo.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/order_history_detail/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_history_detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("orderDetail"));
    }
}
