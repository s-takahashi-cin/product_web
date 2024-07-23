package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.StoreData;
import com.example.demo.repo.StoreRepo;


@WebMvcTest(StoreController.class)
public class StoreControllerTest {
    
        @Autowired
        private MockMvc mockMvc;
    
        @MockBean
        private StoreRepo storeRepo;
    
        @Test
        @WithMockUser(username = "user", roles = {"USER"})
        public void testStoreInfoPage() throws Exception {
            // モックデータの作成
            StoreData store1 = new StoreData();
            store1.setId(1L);
            store1.setName("Store 1");
    
            StoreData store2 = new StoreData();
            store2.setId(2L);
            store2.setName("Store 2");
    
            List<StoreData> stores = Arrays.asList(store1, store2);
    
            // モックの設定
            Mockito.when(storeRepo.findAll()).thenReturn(stores);
    
            // テスト実行
            mockMvc.perform(get("/store_info_page"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("store_info"))
                    .andExpect(model().attributeExists("stores"))
                    .andExpect(model().attribute("stores", stores));
        }
}
