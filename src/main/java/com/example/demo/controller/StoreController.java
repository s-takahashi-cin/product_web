package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.StoreData;
import com.example.demo.repo.StoreRepo;

@RestController
public class StoreController {

    @Autowired
    private StoreRepo storeRepo;

    @GetMapping("/store_info")
    public ResponseEntity<List<StoreData>> topCategory() {
        List<StoreData> storeDatas = storeRepo.findAll();
        if (storeDatas.isEmpty()) {
            return ResponseEntity.status(400).body(null);
        }
        return ResponseEntity.ok(storeDatas);
    }



    @Controller
    public static class ShowStoreController{

        @Autowired
        private StoreRepo storeRepo;
        
        @GetMapping("/store_info_page")
        public String stores(Model model) {
            List<StoreData> stores = storeRepo.findAll();
            model.addAttribute("stores", stores);
            return "store_info";
        }
    }
    
}