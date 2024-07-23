package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.form.SignupForm;
import com.example.demo.repo.AuthorityRepo;
import com.example.demo.repo.PositionRepo;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.UserInfoRepo;
import com.example.demo.service.PositionService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserInfoRepo userInfoRepo;

    @MockBean
    private PositionService positionService;

    @MockBean
    private StoreService storeService;

    @MockBean
    private StoreRepo storeRepo;

    @MockBean
    private PositionRepo positionRepo;

    @MockBean
    private AuthorityRepo authorityRepo;


    @Test
    @WithMockUser(username = "satoutakeru@example.com", roles = "USER")
    public void testSignupAndRedirectToSignin() throws Exception {
        when(userService.addUser(any(SignupForm.class))).thenReturn("ユーザーアカウントが新しく登録されました");
        when(userService.authenticateUser(any(String.class), any(String.class))).thenReturn("Authenticated User");
        UserData mockUser = new UserData();
        mockUser.setEmail("satoutakeru@example.com");
        when(userService.getUserInfo(any(String.class))).thenReturn(mockUser);
    
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

        // ログイン試行
        mockMvc.perform(post("/home")
            .param("email", "satoutakeru@example.com")
            .param("password", "password123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/home"))
            .andExpect(flash().attribute("successMessage", "Logged in successfully!"));

        // ホームページ表示のテスト
        UserData user = new UserData();
        user.setStoreId(1L); // 仮のstoreId
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", user);

        mockMvc.perform(get("/home")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attribute("user", user));

        // 大カテゴリ表示のテスト
        mockMvc.perform(get("/top_categories_page"))
            .andExpect(status().isOk())
            .andExpect(view().name("top_categories"));

        // 中カテゴリ表示のテスト
        Long categoryId = 1L; // 仮のID
        mockMvc.perform(get("/mid_categories_page/{category_id}", categoryId))
            .andExpect(status().isOk())
            .andExpect(view().name("mid_categories"));

        // 小カテゴリ表示のテスト
        Long subCategoryId = 1L; // 仮のID
        mockMvc.perform(get("/low_categories_page/{sub_category_id}", subCategoryId))
            .andExpect(status().isOk())
            .andExpect(view().name("low_categories"));

        // 商品一覧表示のテスト
        mockMvc.perform(get("/products_page/{product_category_id}", 1L)
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("products"))
            .andExpect(model().attributeExists("products"))
            .andExpect(model().attributeExists("productPrices"))
            .andExpect(model().attributeExists("store_id"));

        // 注文フォーム表示のテスト
        // カートのアイテムをセット
        List<OrderDetails> cart = new ArrayList<>();
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProductId(1L);
        orderDetails.setQuantity(2);
        orderDetails.setPrice(100.0);
        orderDetails.setName("商品名");
        orderDetails.setStoreId(1L);
        orderDetails.setLastName("佐藤");
        cart.add(orderDetails);
        session.setAttribute("cart", cart);

        mockMvc.perform(get("/orderForm")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("orderForm"))
            .andExpect(model().attributeExists("user"))
            .andExpect(model().attributeExists("cart"));

        // 注文完了のテスト
        mockMvc.perform(post("/orderComplete")
        .param("storeId", "1")
        .param("id", "1")
        .param("name", "商品名")
        .param("quantities", "2")
        .param("lastName", "佐藤")
        .param("totalAmount", "200.0")
        .param("price", "100.0"))
        .andExpect(status().isOk())
        .andExpect(view().name("orderComplete"));
        
        // 注文履歴ページ表示のテスト
        mockMvc.perform(get("/order_history_page")
        .session(session))
        .andExpect(status().isOk())
        .andExpect(view().name("order_history"))
        .andExpect(model().attributeExists("orders"))
        .andExpect(model().attributeExists("stores"));

        // ストア情報表示のテスト
        StoreData store1 = new StoreData();
        store1.setId(1L);
        store1.setName("Store 1");
        
        StoreData store2 = new StoreData();
        store2.setId(2L);
        store2.setName("Store 2");
        
        List<StoreData> stores = List.of(store1, store2);
        when(storeRepo.findAll()).thenReturn(stores);
        mockMvc.perform(get("/store_info_page"))
        .andExpect(status().isOk())
        .andExpect(view().name("store_info"))
        .andExpect(model().attributeExists("stores"))
        .andExpect(model().attribute("stores", stores));
    }
}
