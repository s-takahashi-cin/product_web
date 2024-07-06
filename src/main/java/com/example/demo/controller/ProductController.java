package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.entity.LowCategory;
import com.example.demo.entity.MidCategory;
import com.example.demo.entity.ProductStorePrice;
import com.example.demo.entity.Products;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.repo.LowCategoryRepo;
import com.example.demo.repo.MidCategoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.ProductStoreRepo;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.TopCategoryRepo;

import jakarta.servlet.http.HttpSession;




@Controller
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
    public String topCategory(Model model) {
        model.addAttribute("top_categories", topCategoryRepo.findAll());
        return "top_categories";
    }

    @GetMapping("/mid_categories/{category_id}")
    public String midCategory(@PathVariable("category_id") Long categoryId, Model model) {
        List<MidCategory> midCategories = midCategoryRepo.findByTopCategoryId(categoryId);
        model.addAttribute("mid_categories", midCategories);
        return "mid_categories";
    }

    @GetMapping("/low_categories/{sub_category_id}")
    public String lowCategory(@PathVariable("sub_category_id") Long subCategoryId, Model model) {
        List<LowCategory> lowCategories = lowCategoryRepo.findBySubCategoryId(subCategoryId);
        model.addAttribute("low_categories", lowCategories);
        return "low_categories";
    }

    @GetMapping("/products/{product_category_id}")
    public String products(@PathVariable("product_category_id") Long productCategoryId, Model model, HttpSession session) {
        UserData user = (UserData) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            System.out.println("商品詳細にログインしているユーザー: " + user.getUsername());
        } else {
            System.out.println("ユーザーがログインしていません。");
            return "redirect:/login";
        }

        Long storeId = user.getStoreId();

        StoreData storeData = storeRepo.getStoreDataById(storeId);

        List<Products> products = productRepo.findByProductCategoryId(productCategoryId);
        if (products.isEmpty()) {
            return "noItem";
        }

        Map<Long, Double> productPrices = new HashMap<>();
        for (Products product : products) {
            ProductStorePrice storePrice = productStoreRepo.findByProductAndStoreData(product, storeData);
            if (storePrice != null) {
                productPrices.put(product.getId(), storePrice.getPrice());
                System.out.println("店舗ID " + storeData.getId() + " の価格を設定しました。");
            } else {
                // デフォルト価格を設定（店舗に関連付けられていない場合）
                productPrices.put(product.getId(), product.getRetailPrice());
                System.out.println("店舗ID " + storeData.getId() + " に関連付けられた価格が見つかりませんでした。デフォルト価格を設定しました。");

            }
        }


        model.addAttribute("products", products);
        model.addAttribute("productPrices", productPrices);

        return "products";
    }
}
