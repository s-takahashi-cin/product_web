package com.example.demo.controller;

import com.example.demo.entity.UserData;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.ManufacturerRepo;
import com.example.demo.repo.PositionRepo;
import com.example.demo.repo.AuthorityRepo;
import com.example.demo.repo.UserInfoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.Mockito;

import com.example.demo.entity.AuthorityData;
import com.example.demo.entity.ManufacturerData;
import com.example.demo.entity.PositionData;
import com.example.demo.entity.StoreData;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInfoRepo userInfoRepo;

    @MockBean
    private StoreRepo storeRepo;

    @MockBean
    private ManufacturerRepo manufacturerRepo;

    @MockBean
    private PositionRepo positionRepo;

    @MockBean
    private AuthorityRepo authorityRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testHome() throws Exception {
        UserData user = new UserData();
        user.setId(1L);
        user.setEmail("user@example.com");  // getUsername() は email を返す

        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                .sessionAttr("user", user))  // セッションにユーザーをセット
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUserInfoFound() throws Exception {
        UserData user = new UserData();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setStoreId(1L);
        user.setPositionId(1L);
        user.setAuthorityId(1L);

        StoreData store = new StoreData();
        store.setId(1L);
        store.setName("Test Store");

        PositionData position = new PositionData();
        position.setId(1L);
        position.setName("Test Position");

        AuthorityData authority = new AuthorityData();
        authority.setId(1L);
        authority.setName("Test Authority");

        Mockito.when(userInfoRepo.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(storeRepo.findById(1L)).thenReturn(Optional.of(store));
        Mockito.when(positionRepo.findById(1L)).thenReturn(Optional.of(position));
        Mockito.when(authorityRepo.findById(1L)).thenReturn(Optional.of(authority));

        mockMvc.perform(get("/user_info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user_info"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testManufacturers() throws Exception {
        // モックデータの作成
        ManufacturerData manufacturer1 = new ManufacturerData();
        manufacturer1.setId(1L);
        manufacturer1.setName("Manufacturer 1");

        ManufacturerData manufacturer2 = new ManufacturerData();
        manufacturer2.setId(2L);
        manufacturer2.setName("Manufacturer 2");

        List<ManufacturerData> manufacturers = Arrays.asList(manufacturer1, manufacturer2);

        // モックの設定
        Mockito.when(manufacturerRepo.findAll()).thenReturn(manufacturers);

        // テスト実行
        mockMvc.perform(get("/manufacturer"))
                .andExpect(status().isOk())
                .andExpect(view().name("manufacturer"))
                .andExpect(model().attributeExists("manufacturers"))
                .andExpect(model().attribute("manufacturers", manufacturers));
    }
}
