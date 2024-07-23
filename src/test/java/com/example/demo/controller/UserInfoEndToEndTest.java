package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.AuthorityData;
import com.example.demo.entity.PositionData;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.form.EditForm;
import com.example.demo.repo.AuthorityRepo;
import com.example.demo.repo.PositionRepo;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.UserInfoRepo;
import com.example.demo.service.PositionService;
import com.example.demo.service.UserService;



@SpringBootTest
@AutoConfigureMockMvc
public class UserInfoEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInfoRepo userInfoRepo;

    @MockBean
    private StoreRepo storeRepo;

    @MockBean
    private PositionRepo positionRepo;

    @MockBean
    private AuthorityRepo authorityRepo;

    @MockBean
    private PositionService positionService;

    @MockBean
    private UserService userService;

    @Test
    public void testUserInfo() throws Exception {
        // ユーザー情報表示の準備
        UserData testUser = new UserData();
        testUser.setId(1L);
        testUser.setStoreId(1L);
        testUser.setPositionId(1L);
        testUser.setAuthorityId(1L);

        StoreData storeData = new StoreData();
        storeData.setId(1L);
        storeData.setName("Store Name");

        PositionData positionData = new PositionData();
        positionData.setId(1L);
        positionData.setName("Position Name");

        AuthorityData authorityData = new AuthorityData();
        authorityData.setId(1L);
        authorityData.setName("Authority Name");

        // 編集フォームの準備
        EditForm editForm = new EditForm();
        editForm.setId(testUser.getId());
        editForm.setLastName(testUser.getLastName());
        editForm.setFirstName(testUser.getFirstName());
        editForm.setEmail(testUser.getEmail());
        editForm.setPhone(testUser.getPhone());
        editForm.setStoreId(testUser.getStoreId());
        editForm.setPositionId(testUser.getPositionId());

        // モックの設定
        when(userInfoRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(storeRepo.findById(1L)).thenReturn(Optional.of(storeData));
        when(positionRepo.findById(1L)).thenReturn(Optional.of(positionData));
        when(authorityRepo.findById(1L)).thenReturn(Optional.of(authorityData));

        // ユーザー情報表示のテスト
        mockMvc.perform(get("/user_info/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(view().name("user_info"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", testUser));
    }
}