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

@WebMvcTest(ProductController.MidCategoryController.class)
@ActiveProfiles("test")
public class MidCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowMidCategoriesPage() throws Exception {
        Long categoryId = 1L;  // テスト対象のカテゴリIDを指定
        mockMvc.perform(MockMvcRequestBuilders.get("/mid_categories_page/" + categoryId))
            .andExpect(status().isOk())
            .andExpect(view().name("mid_categories"));
    }
}
