package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.*;
import org.springframework.web.servlet.FlashMap;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.form.SignupForm;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


public class SignupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private SignupController signupController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(signupController)
                .setViewResolvers(viewResolver())
                .build();
    }

    @SpringBootTest
    public class ProductWebsiteApplicationTests {

        @Test
        void contextLoads() {
        }
    }

    private ThymeleafViewResolver viewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Test
    public void testShowSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("signupForm"));
    }

    @Test
    public void testSignup_Success() throws Exception {
        when(userService.addUser(any())).thenReturn("ユーザーアカウントが新しく登録されました");

        mockMvc.perform(post("/signup")
            .param("lastName", "Test")
            .param("firstName", "User")
            .param("email", "test12@example.com")
            .param("password", "password123")
            .param("authorityId", "1")
            .param("positionId", "1")
            .param("storeId", "1")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/signin"))
            .andExpect(flash().attribute("successMessage", "サインアップ成功しました")); 
    }
    

    @Test
    public void testSignup_Failure() throws Exception {
        when(userService.addUser(any(SignupForm.class))).thenReturn("記入に誤りがあります");
    
        MvcResult result = mockMvc.perform(post("/signup")
                .param("lastName", "")
                .param("firstName", "")
                .param("email", "testuser@example.com")
                .param("password", "password123")
                .param("authorityId", "1")
                .param("positionId", "1")
                .param("storeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/signup"))
                .andExpect(flash().attribute("errorMessage", "記入に誤りがあります"))
                .andReturn();
    
        // FlashAttributesの検証
        FlashMap flashMap = result.getFlashMap();
        assertNotNull(flashMap);
        assertEquals("記入に誤りがあります", flashMap.get("errorMessage"));
    }
    
    
}