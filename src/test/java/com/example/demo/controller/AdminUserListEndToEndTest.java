package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.example.demo.entity.PositionData;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.form.EditForm;
import com.example.demo.repo.PositionRepo;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.UserInfoRepo;
import com.example.demo.service.PositionService;
import com.example.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminUserListEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserInfoRepo userInfoRepo;

    @MockBean
    private StoreRepo storeRepo;

    @MockBean
    private PositionRepo positionRepo;

    @MockBean
    private PositionService positionService;

    @MockBean
    private UserService userService;

    @Test
    public void testAdminList() throws Exception {
        // 管理者用従業員一覧表示の準備
        UserData admin = new UserData();
        admin.setAuthorityId(1L); // 管理者の authorityId
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("user", admin);

        // ポジションデータの準備
        PositionData position1 = new PositionData();
        position1.setId(1L);
        position1.setName("Manager");

        PositionData position2 = new PositionData();
        position2.setId(2L);
        position2.setName("Assistant Manager");

        // モックデータの準備
        UserData user1 = new UserData();
        user1.setId(1L);
        user1.setLastName("田中");
        user1.setFirstName("太郎");
        user1.setEmail("taro@example.com");

        UserData user2 = new UserData();
        user2.setId(2L);
        user2.setLastName("鈴木");
        user2.setFirstName("次郎");
        user2.setEmail("jiro@example.com");

        List<UserData> users = List.of(user1, user2);

        when(userService.getUserInfoForAdmin()).thenReturn(users);
        when(positionService.getAllPositions()).thenReturn(List.of(position1, position2));

        // 管理者用従業員一覧表示のテスト
        mockMvc.perform(get("/admin_list")
            .session(adminSession))
            .andExpect(status().isOk())
            .andExpect(view().name("admin_list"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("users", users));
    }

    @Test
    public void testEditUserForm() throws Exception {
        // 編集フォームの準備
        UserData testUser = new UserData();
        testUser.setId(1L);
        testUser.setLastName("田中");
        testUser.setFirstName("太郎");
        testUser.setEmail("taro@example.com");
        testUser.setPhone("123456789");
        testUser.setStoreId(1L);
        testUser.setPositionId(1L);

        EditForm editForm = new EditForm();
        editForm.setId(testUser.getId());
        editForm.setLastName(testUser.getLastName());
        editForm.setFirstName(testUser.getFirstName());
        editForm.setEmail(testUser.getEmail());
        editForm.setPhone(testUser.getPhone());
        editForm.setStoreId(testUser.getStoreId());
        editForm.setPositionId(testUser.getPositionId());

        StoreData storeData = new StoreData();
        storeData.setId(1L);
        storeData.setName("Store Name");

        PositionData positionData = new PositionData();
        positionData.setId(1L);
        positionData.setName("Position Name");

        // モックの設定
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(positionService.getAllPositions()).thenReturn(List.of(positionData));
        when(storeRepo.findAll()).thenReturn(List.of(storeData));

        // 編集フォーム表示のテスト
        mockMvc.perform(get("/edit/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(view().name("edit"))
            .andExpect(model().attributeExists("editForm"))
            .andExpect(model().attribute("editForm", editForm))
            .andExpect(model().attributeExists("positions"))
            .andExpect(model().attributeExists("stores"));
    }

    @Test
    public void testEditUser() throws Exception {
        EditForm editForm = new EditForm();
        editForm.setId(1L);
        editForm.setLastName("田中");
        editForm.setFirstName("太郎");
        editForm.setEmail("taro@example.com");
        editForm.setPhone("123456789");
        editForm.setStoreId(1L);
        editForm.setPositionId(1L);

        // 編集操作のモック設定
        doNothing().when(userService).editUser(eq(1L), any(EditForm.class));

        // 編集処理のテスト
        mockMvc.perform(post("/edit/{id}", 1L)
        .flashAttr("editForm", editForm))
        .andExpect(status().isOk())  // ステータスコードは200 OKを期待
        .andExpect(view().name("editComplete"))  // ビュー名は"editComplete"
        .andExpect(model().attributeExists("editForm"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // 削除操作のモック設定
        doNothing().when(userService).deleteById(1L);

        // 削除処理のテスト
        mockMvc.perform(get("/delete/{id}", 1L))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin_list"));
    }
}
