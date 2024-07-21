package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.TopCategoryController.class)
@ActiveProfiles("test")
public class TopCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowTopCategoriesPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/top_categories_page"))
            .andExpect(status().isOk())
            .andExpect(view().name("top_categories"));
    }

    // @Test
    // @WithMockUser(username = "testuser", roles = {"USER"})
    // public void testShowMidCategoriesPage() throws Exception {
    //     Long categoryId = 1L;
    //     mockMvc.perform(MockMvcRequestBuilders.get("/mid_categories_page/" + categoryId))
    //         .andExpect(status().isOk())
    //         .andExpect(view().name("mid_categories"));
    // }
}
