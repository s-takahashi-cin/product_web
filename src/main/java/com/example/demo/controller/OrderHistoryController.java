package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.form.OrderForm;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.StoreRepo;

import jakarta.servlet.http.HttpSession;

@RestController
public class OrderHistoryController {

    @Autowired
    private OrderFormRepo orderFormRepo;

    @Autowired
    private StoreRepo storeRepo;

    @GetMapping("/order_history")
    public ResponseEntity<?> orderHistory(@RequestParam(name = "storeId", required = false) Long storeId, HttpSession session) {
        UserData user = (UserData) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("ユーザーがログインしていません。");
        }

        List<OrderForm> orders;
        if (storeId == null) {
            orders = orderFormRepo.findAll();
        } else {
            orders = orderFormRepo.findByStoreId(storeId);
        }

        List<Map<String, Object>> orderDetails = new ArrayList<>();
        for (OrderForm order : orders) {
            Map<String, Object> orderDetail = new HashMap<>();
            orderDetail.put("orderId", order.getId());
            orderDetail.put("storeName", storeRepo.findById(order.getStoreId()).map(StoreData::getName).orElse(null));
            orderDetail.put("totalAmount", order.getTotalAmount());
            orderDetail.put("createdAt", order.getCreatedAt());
            orderDetail.put("lastName", order.getLastName());

            orderDetails.add(orderDetail);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("lastName", user.getLastName());
        response.put("orders", orderDetails);

        return ResponseEntity.ok(response);
    }

    @Controller
    public static class ShowOrderHistoryController {

        @Autowired
        private OrderFormRepo orderFormRepo;

        @Autowired
        private StoreRepo storeRepo;

        @GetMapping("/order_history_page")
        public String orderHistoryPage(Model model) {
            List<OrderForm> orders = orderFormRepo.findAll();

            // 注文ごとの店舗名をセットする
            for (OrderForm order : orders) {
                StoreData storeData = storeRepo.findById(order.getStoreId()).orElse(null);
                if (storeData != null) {
                    order.setStoreName(storeData.getName());
                }
            }

            // 全ての店舗情報を取得してモデルに追加する
            List<StoreData> stores = storeRepo.findAll();
            model.addAttribute("orders", orders);
            model.addAttribute("stores", stores);
            return "order_history";
        }

        @GetMapping("/order_history_detail/{id}")
        public String orderDetail(@PathVariable("id") Long id, Model model) {
            Optional<OrderForm> orderDataOptional = orderFormRepo.findById(id);
            if (orderDataOptional.isPresent()) {
                OrderForm orderDetail = orderDataOptional.get();
                model.addAttribute("orderDetail", orderDetail);
                return "order_history_detail";
            } else {
                System.err.println("Order not found for ID: " + id);
                return "order_not_found";
            }
        }
    }
}
