package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entity.PositionData;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.form.EditForm;
import com.example.demo.service.PositionService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PositionService positionService;

    @MockBean
    private StoreService storeService;

    @Test
    public void testAdminListPageDisplaysUsers() throws Exception {
        UserData user1 = new UserData();
        user1.setId(1L);
        user1.setLastName("Doe");
        user1.setFirstName("John");

        UserData user2 = new UserData();
        user2.setId(2L);
        user2.setLastName("Smith");
        user2.setFirstName("Jane");

        List<UserData> mockUsers = Arrays.asList(user1, user2);

        when(userService.getUserInfoForAdmin()).thenReturn(mockUsers);

        UserData admin = new UserData();
        admin.setAuthorityId(1L); // 管理者の authorityId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", admin);

        mockMvc.perform(get("/admin_list")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("admin_list"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("users", mockUsers));
    }
    @Test
    public void testEditUserPageDisplaysUserInfo() throws Exception {
        Long userId = 1L;
        UserData mockUser = new UserData();
        mockUser.setId(userId);
        mockUser.setLastName("Doe");
        mockUser.setFirstName("John");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPhone("123-456-7890");
        mockUser.setStoreId(1L);
        mockUser.setPositionId(1L);
    
        EditForm editForm = new EditForm();
        editForm.setId(mockUser.getId());
        editForm.setLastName(mockUser.getLastName());
        editForm.setFirstName(mockUser.getFirstName());
        editForm.setEmail(mockUser.getEmail());
        editForm.setPhone(mockUser.getPhone());
        editForm.setStoreId(mockUser.getStoreId());
        editForm.setPositionId(mockUser.getPositionId());
    
        PositionData position = new PositionData();
        position.setId(1L);
        position.setName("Manager");
    
        StoreData store = new StoreData();
        store.setId(1L);
        store.setName("Store1");
        store.setAddress("Address1");
    
        List<PositionData> positions = Arrays.asList(position);
        List<StoreData> stores = Arrays.asList(store);
    
        when(userService.getUserById(userId)).thenReturn(mockUser);
        when(positionService.getAllPositions()).thenReturn(positions);
        when(storeService.getAllStores()).thenReturn(stores);
    
        UserData admin = new UserData();
        admin.setAuthorityId(1L); // 管理者の authorityId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", admin);
    
        mockMvc.perform(get("/edit/{id}", userId)
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("edit"))
            .andExpect(model().attributeExists("editForm"))
            .andExpect(model().attribute("editForm", editForm))
            .andExpect(model().attributeExists("positions"))
            .andExpect(model().attribute("positions", positions))
            .andExpect(model().attributeExists("stores"))
            .andExpect(model().attribute("stores", stores));
    }
    

    @Test
    public void testSaveEditedUser() throws Exception {
        Long userId = 1L;
        EditForm editForm = new EditForm();
        editForm.setId(userId);
        editForm.setLastName("Doe");
        editForm.setFirstName("John");
        editForm.setEmail("john.doe@example.com");
        editForm.setPhone("123-456-7890");
        editForm.setStoreId(1L);
        editForm.setPositionId(1L);
    
        // 修正: eg(userId) を userId に変更
        doNothing().when(userService).editUser(eq(userId), any(EditForm.class));
    
        UserData admin = new UserData();
        admin.setAuthorityId(1L); // 管理者の authorityId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", admin);
    
        mockMvc.perform(post("/edit/{id}", userId)
                .session(session)
                .flashAttr("editForm", editForm))
            .andExpect(status().isOk())
            .andExpect(view().name("editComplete"))
            .andExpect(model().attributeExists("editForm"))
            .andExpect(model().attribute("editForm", editForm));
    }
    

    @Test
    public void testDeleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteById(userId);

        UserData admin = new UserData();
        admin.setAuthorityId(1L); // 管理者の authorityId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", admin);

        mockMvc.perform(get("/delete/{id}", userId)
                .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin_list"));
    }
}

