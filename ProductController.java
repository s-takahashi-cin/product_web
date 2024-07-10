package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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



@RestController
@RequestMapping("/api")
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

    // @GetMapping("/mid_categories/{category_id}")
    // public ResponseEntity<List<MidCategory>> midCategory(@PathVariable("category_id") Long categoryId) {
    //     List<MidCategory> midCategories = midCategoryRepo.findByTopCategoryId(categoryId);
    //     if (midCategories.isEmpty()) {
    //         return ResponseEntity.status(400).body(null);
    //     }
    //     return ResponseEntity.ok(midCategories);
    // }

    // @GetMapping("/low_categories/{sub_category_id}")
    // public ResponseEntity<List<LowCategory>> lowCategory(@PathVariable("sub_category_id") Long subCategoryId) {
    //     List<LowCategory> lowCategories = lowCategoryRepo.findBySubCategoryId(subCategoryId);
    //     if (lowCategories.isEmpty()) {
    //         return ResponseEntity.status(400).body(null);
    //     }
    //     return ResponseEntity.ok(lowCategories);
    // }

    // @GetMapping("/products/{product_category_id}")
    // public ResponseEntity<?> products(@PathVariable("product_category_id") Long productCategoryId, HttpSession session) {
    //     UserData user = (UserData) session.getAttribute("user");
    //     if (user == null) {
    //         return ResponseEntity.status(401).body("ユーザーがログインしていません。");
    //     }

    //     Long storeId = user.getStoreId();
    //     StoreData storeData = storeRepo.getStoreDataById(storeId);

    //     List<Products> products = productRepo.findByProductCategoryId(productCategoryId);
    //     if (products.isEmpty()) {
    //         return ResponseEntity.status(400).body("商品が見つかりませんでした。");
    //     }

    //     Map<Long, Double> productPrices = new HashMap<>();
    //     for (Products product : products) {
    //         ProductStorePrice storePrice = productStoreRepo.findByProductAndStoreData(product, storeData);
    //         if (storePrice != null) {
    //             productPrices.put(product.getId(), storePrice.getPrice());
    //         } else {
    //             productPrices.put(product.getId(), product.getRetailPrice());
    //         }
    //     }

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("userId", user.getId());
    //     response.put("userName", user.getLastName());
    
    //     List<Map<String, Object>> productDetails = new ArrayList<>();
    //     for (Products product : products) {
    //         Map<String, Object> productDetail = new HashMap<>();
    //         productDetail.put("productId", product.getId());
    //         productDetail.put("productName", product.getName());
    //         productDetail.put("productPrice", productPrices.get(product.getId()));
    //         productDetails.add(productDetail);
    //     }
    //     response.put("products", productDetails);
    //     return ResponseEntity.ok(response);
    // }

}

