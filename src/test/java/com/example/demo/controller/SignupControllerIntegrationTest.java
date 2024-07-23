package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.UserData;
import com.example.demo.form.SignupForm;
import com.example.demo.repo.UserInfoRepo;
import com.example.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class SignupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserInfoRepo userInfoRepo;

   
    @Test
    public void testSignupAndRedirectToSignin() throws Exception {
        // モックの設定
        when(userService.addUser(any(SignupForm.class))).thenReturn("ユーザーアカウントが新しく登録されました");
        when(userService.authenticateUser(any(String.class), any(String.class))).thenReturn("Authenticated User");  // ここで返す値を設定
        UserData mockUser = new UserData();
        mockUser.setEmail("satoutakeru@example.com");
        when(userService.getUserInfo(any(String.class))).thenReturn(new UserData());
    
        // ユーザー登録フォームの送信
        mockMvc.perform(post("/signup")
            .param("lastName", "佐藤")
            .param("firstName", "たける")
            .param("email", "satoutakeru@example.com")
            .param("password", "password123")
            .param("phoneNumber", "09012345678")
            .param("authorityId", "1")
            .param("positionId", "1")
            .param("storeId", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/signin"))
            .andExpect(flash().attributeExists("successMessage"));

        // ログインテスト
        mockMvc.perform(post("/home")
            .param("email", "satoutakeru@example.com")
            .param("password", "password123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/home"))
            .andExpect(flash().attribute("successMessage", "Logged in successfully!"));  // 成功メッセージの確認
    }
    
}
