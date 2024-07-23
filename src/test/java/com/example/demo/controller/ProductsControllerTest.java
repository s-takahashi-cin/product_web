package com.example.demo.controller;

import com.example.demo.entity.ProductStorePrice;
import com.example.demo.entity.Products;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.ProductStoreRepo;
import com.example.demo.repo.StoreRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(ProductController.ProductsController.class)
@ActiveProfiles("test")
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepo productRepo;

    @MockBean
    private ProductStoreRepo productStoreRepo;

    @MockBean
    private StoreRepo storeRepo;

    @MockBean
    private HttpSession session;

   
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowProductPage() throws Exception {
        Long productCategoryId = 1L;  // テスト対象のプロダクトカテゴリIDを指定
        UserData user = mock(UserData.class);
        when(session.getAttribute("user")).thenReturn(user);
        when(user.getStoreId()).thenReturn(1L);

        StoreData storeData = mock(StoreData.class);
        when(storeRepo.getStoreDataById(1L)).thenReturn(storeData);

        List<Products> products = new ArrayList<>();
        Products product = mock(Products.class);
        when(product.getId()).thenReturn(1L);
        when(product.getName()).thenReturn("Product1");
        when(product.getBody()).thenReturn("Product1 Body");
        products.add(product);

        when(productRepo.findByProductCategoryId(productCategoryId)).thenReturn(products);

        ProductStorePrice storePrice = mock(ProductStorePrice.class);
        when(storePrice.getPrice()).thenReturn(100.0);
        when(productStoreRepo.findByProductAndStoreData(product, storeData)).thenReturn(storePrice);

        // テスト対象のエンドポイントにGETリクエストを送信
        mockMvc.perform(get("/products_page/" + productCategoryId)
                .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("productPrices"))
                .andExpect(model().attributeExists("store_id"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowProductPageNoUser() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        Long productCategoryId = 1L;  // テスト対象のプロダクトカテゴリIDを指定

        // テスト対象のエンドポイントにGETリクエストを送信
        mockMvc.perform(get("/products_page/" + productCategoryId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/noItem"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testShowProductPageNoProducts() throws Exception {
        Long productCategoryId = 1L;  // テスト対象のプロダクトカテゴリIDを指定
        UserData user = mock(UserData.class);
        when(session.getAttribute("user")).thenReturn(user);
        when(user.getStoreId()).thenReturn(1L);
        when(productRepo.findByProductCategoryId(productCategoryId)).thenReturn(new ArrayList<>());

        // テスト対象のエンドポイントにGETリクエストを送信
        mockMvc.perform(get("/products_page/" + productCategoryId)
                .sessionAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/noItem"));
    }



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testNoItemPage() throws Exception {
        // テスト対象のエンドポイントにGETリクエストを送信
        mockMvc.perform(get("/noItem"))
                .andExpect(status().isOk())
                .andExpect(view().name("noItem"));
    }
}
