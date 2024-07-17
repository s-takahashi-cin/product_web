package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.LowCategory;
import com.example.demo.entity.MidCategory;
import com.example.demo.entity.ProductStorePrice;
import com.example.demo.entity.Products;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.TopCategory;
import com.example.demo.entity.UserData;
import com.example.demo.repo.LowCategoryRepo;
import com.example.demo.repo.MidCategoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.ProductStoreRepo;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.TopCategoryRepo;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class ProductController {

    @Autowired
    private final TopCategoryRepo topCategoryRepo;
    private final MidCategoryRepo midCategoryRepo;
    private final LowCategoryRepo lowCategoryRepo;
    private final ProductRepo productRepo;
    private final ProductStoreRepo productStoreRepo;
    private final StoreRepo storeRepo;

    public ProductController(StoreRepo storeRepo, ProductStoreRepo productStoreRepo, TopCategoryRepo topCategoryRepo, MidCategoryRepo midCategoryRepo, LowCategoryRepo lowCategoryRepo, ProductRepo productRepo) {
        this.topCategoryRepo = topCategoryRepo;
        this.midCategoryRepo = midCategoryRepo;
        this.lowCategoryRepo = lowCategoryRepo;
        this.productRepo = productRepo;
        this.productStoreRepo = productStoreRepo;
        this.storeRepo = storeRepo;
    }

    @GetMapping("/top_categories")
    public ResponseEntity<List<TopCategory>> topCategory() {
        List<TopCategory> topCategories = topCategoryRepo.findAll();
        if (topCategories.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(topCategories);
    }

    @Controller
    public static class TopCategoryController {
        @GetMapping("/top_categories_page")
        public String showTopCategoriesPage() {
            return "top_categories";
        }
    }

    @GetMapping("/mid_categories/{category_id}")
    public ResponseEntity<List<MidCategory>> midCategory(@PathVariable("category_id") Long categoryId) {
        List<MidCategory> midCategories = midCategoryRepo.findByTopCategoryId(categoryId);
        if (midCategories.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(midCategories);
    }

    @Controller
    public static class MidCategoryController {
        @GetMapping("/mid_categories_page/{category_id}")
        public String showMidCategoriesPage(@PathVariable("category_id") Long categoryId) {
            return "mid_categories";
        }
    }

    @GetMapping("/low_categories/{sub_category_id}")
    public ResponseEntity<List<LowCategory>> lowCategory(@PathVariable("sub_category_id") Long subCategoryId) {
        List<LowCategory> lowCategories = lowCategoryRepo.findBySubCategoryId(subCategoryId);
        if (lowCategories.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(lowCategories);
    }

    @Controller
    public static class LowCategoryController {
        @GetMapping("/low_categories_page/{sub_category_id}")
        public String showLowCategoriesPage(@PathVariable("sub_category_id") Long subCategoryId) {
            return "low_categories";
        }
    }

    @GetMapping("/products/{product_category_id}")
    public ResponseEntity<?> products(@PathVariable("product_category_id") Long productCategoryId, HttpSession session) {
        System.out.println("商品ページの小カテゴリID: " + productCategoryId);
    
        UserData user = (UserData) session.getAttribute("user");
        if (user != null) {
            // ログインユーザーの情報を表示
            System.out.println("商品ページにログインしているユーザー: " + user.getUsername());
    
            // ユーザーのstore_idを取得
            Long userStoreId = user.getStoreId();
            Long storeId = userStoreId;
            System.out.println("使用する店舗ID: " + storeId);
    
            StoreData storeData = storeRepo.getStoreDataById(storeId);
    
            List<Products> products = productRepo.findByProductCategoryId(productCategoryId);
            System.out.println("取得した商品リスト: " + products);
            if (products.isEmpty()) {
                return ResponseEntity.status(400).body("商品が見つかりませんでした。");
            }
    
            Map<Long, Double> productPrices = new HashMap<>();
            for (Products product : products) {
                ProductStorePrice storePrice = productStoreRepo.findByProductAndStoreData(product, storeData);
                if (storePrice != null) {
                    System.out.println("店舗ごとの値段: " + storePrice.getPrice());
                    productPrices.put(product.getId(), storePrice.getPrice());
                } else {
                    productPrices.put(product.getId(), product.getRetailPrice()); // 価格が見つからない場合は標準価格を使用
                }
            }
    
            List<Map<String, Object>> productDetails = new ArrayList<>();
            for (Products product : products) {
                Map<String, Object> productDetail = new HashMap<>();
                productDetail.put("productId", product.getId());
                productDetail.put("productName", product.getName());
                productDetail.put("productBody", product.getBody());
                Double price = productPrices.get(product.getId()); // 価格を取得
                System.out.println("商品ID: " + product.getId() + " の価格: " + price);
                productDetail.put("price", price);
                productDetails.add(productDetail);
            }
    
            Map<String, Object> response = new HashMap<>();
            response.put("store_id", storeId);
            response.put("products", productDetails);
            return ResponseEntity.ok(response);
        } else {
            // ユーザーがログインしていない場合の処理
            System.out.println("ユーザーがログインしていません。");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ログインが必要です。");
        }
    }
    
    @Controller
    public static class ProductsController {
        @Autowired
        private ProductRepo productRepo;
    
        @Autowired
        private ProductStoreRepo productStoreRepo;
    
        @Autowired
        private StoreRepo storeRepo;
    
        @GetMapping("/products_page/{product_category_id}")
        public String showProductPage(@PathVariable("product_category_id") Long productCategoryId, Model model, HttpSession session) {
            UserData user = (UserData) session.getAttribute("user");
            if (user == null) {
                return "redirect:/noItem";
            }
    
            Long userStoreId = user.getStoreId();
            StoreData storeData = storeRepo.getStoreDataById(userStoreId);
    
            List<Products> products = productRepo.findByProductCategoryId(productCategoryId);
            if (products.isEmpty()) {
                return "redirect:/noItem";
            }
    
            Map<Long, Double> productPrices = new HashMap<>();
            for (Products product : products) {
                ProductStorePrice storePrice = productStoreRepo.findByProductAndStoreData(product, storeData);
                if (storePrice != null) {
                    productPrices.put(product.getId(), storePrice.getPrice());
                } else {
                    productPrices.put(product.getId(), product.getRetailPrice()); // 価格が見つからない場合は標準価格を使用
                }
            }
    
            model.addAttribute("products", products);
            model.addAttribute("productPrices", productPrices);
            model.addAttribute("store_id", userStoreId);
            return "products";
        }

        @GetMapping("/noItem")
        public String noItem() {
            return "noItem";
        }
    }


}

