package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProductController.LowCategoryController.class)
@ActiveProfiles("test")
public class LowCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowLowCategoriesPage() throws Exception {
        Long subCategoryId = 1L;  // テスト対象のサブカテゴリIDを指定
        mockMvc.perform(MockMvcRequestBuilders.get("/low_categories_page/" + subCategoryId))
            .andExpect(status().isOk())
            .andExpect(view().name("low_categories"));
    }
}
