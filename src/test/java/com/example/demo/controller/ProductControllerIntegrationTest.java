package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.UserData;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testTopCategoriesPage() throws Exception {
        mockMvc.perform(get("/top_categories_page"))
            .andExpect(status().isOk())
            .andExpect(view().name("top_categories"));
    }

    @Test
    public void testMidCategoriesPage() throws Exception {
        Long categoryId = 1L; // 仮のID
        mockMvc.perform(get("/mid_categories_page/{category_id}", categoryId))
            .andExpect(status().isOk())
            .andExpect(view().name("mid_categories"));
    }

    @Test
    public void testLowCategoriesPage() throws Exception {
        Long subCategoryId = 1L; // 仮のID
        mockMvc.perform(get("/low_categories_page/{sub_category_id}", subCategoryId))
            .andExpect(status().isOk())
            .andExpect(view().name("low_categories"));
    }

    @Test
    public void testProductsPage() throws Exception {
        UserData user = new UserData();
        user.setStoreId(1L); // 仮のstoreId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        Long productCategoryId = 1L; // 仮のID
        mockMvc.perform(get("/products_page/{product_category_id}", productCategoryId)
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("products"))
            .andExpect(model().attributeExists("products"))
            .andExpect(model().attributeExists("productPrices"))
            .andExpect(model().attributeExists("store_id"));
    }

    @Test
    public void testNoItemPage() throws Exception {
        mockMvc.perform(get("/noItem"))
            .andExpect(status().isOk())
            .andExpect(view().name("noItem"));
    }

    @Test
    public void testProductsApi() throws Exception {
        UserData user = new UserData();
        user.setStoreId(1L); // 仮のstoreId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        Long productCategoryId = 1L; // 仮のID
        mockMvc.perform(get("/products/{product_category_id}", productCategoryId)
                .session(session))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("productId")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("price")));
    }
}
