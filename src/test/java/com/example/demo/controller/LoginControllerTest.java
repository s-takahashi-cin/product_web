package com.example.demo.controller;

import com.example.demo.entity.UserData;
import com.example.demo.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testSignin_Success() throws Exception {
        when(userService.authenticateUser(anyString(), anyString())).thenReturn("Authenticated User");
        when(userService.getUserInfo(anyString())).thenReturn(new UserData());

        mockMvc.perform(post("/home")
            .param("email", "test@example.com")
            .param("password", "password123")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/home"))
        .andExpect(flash().attribute("successMessage", "Logged in successfully!"));
    }

    @Test
    public void testSignin_Failure() throws Exception {
        when(userService.authenticateUser(anyString(), anyString())).thenReturn("Invalid credentials");

        mockMvc.perform(post("/home")
            .param("email", "test@example.com")
            .param("password", "wrongpassword")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/signin"))
        .andExpect(flash().attribute("errorMessage", "Invalid credentials"));
    }
}
