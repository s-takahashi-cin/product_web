package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Products;
import com.example.demo.entity.UserData;
import com.example.demo.form.OrderForm;
import com.example.demo.repo.OrderDetailRepo;
import com.example.demo.repo.OrderFormRepo;
import com.example.demo.repo.ProductRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@SessionAttributes("cart")
public class OrderController {

    @Autowired
    private OrderFormRepo orderFormRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private ProductRepo productRepo;

    @ModelAttribute("cart")
    public List<OrderDetails> createCart() {
        return new ArrayList<>();
    }
    
    @GetMapping("/orderForm")
    public String orderForm(Model model, HttpSession session) {
        UserData user = (UserData) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }
    
        // セッションからカートのデータを取得
        List<OrderDetails> cart = (List<OrderDetails>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>(); // カートが null の場合、空のリストを作成
        }
        model.addAttribute("cart", cart);
        return "orderForm";
    }

    @Transactional
    @PostMapping("/orderComplete")
    public String orderComplete(@RequestParam("storeId") Long storeId,
                                @RequestParam("id") String id,
                                @RequestParam("name") String name, //商品名
                                @RequestParam("quantities") List<Integer> quantities,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("totalAmount") double totalAmount,
                                @RequestParam("price") String price,
                                Model model,
                                @ModelAttribute("cart") List<OrderDetails> cart) {

        String[] productIdArray = id.split(",");
        String[] priceArray = price.split(",");

        if (productIdArray.length != quantities.size() || productIdArray.length != priceArray.length) {
            throw new IllegalArgumentException("商品ID、数量、料金が一致しません");
        }

        OrderForm orderForm = new OrderForm();
        orderForm.setStoreId(storeId);
        orderForm.setLastName(lastName);
        orderForm.setTotalAmount(BigDecimal.valueOf(totalAmount));
        orderFormRepo.save(orderForm);

        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (int i = 0; i < productIdArray.length; i++) {
            Long productId;
            if (productIdArray[i].trim().isEmpty()) {
                continue;
            }
            try {
                productId = Long.parseLong(productIdArray[i].trim());
            } catch (NumberFormatException e) {
                continue;
            }
            Integer quantity = quantities.get(i);
            
            Products product = productRepo.findById(productId).orElse(null);
            if (product != null) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setStoreId(storeId);
                orderDetails.setOrderForm(orderForm);
                orderDetails.setProductId(productId);
                orderDetails.setName(product.getName());
                orderDetails.setQuantity(quantity);
                orderDetails.setLastName(lastName);
                
                BigDecimal totalAmt = BigDecimal.valueOf(totalAmount);
                orderDetails.setTotalAmount(totalAmt.doubleValue());
                
                
                BigDecimal productPrice = priceArray[i].isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceArray[i]);
                orderDetails.setPrice(productPrice.doubleValue());
                try {
                    orderDetailRepo.save(orderDetails);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                orderDetailsList.add(orderDetails);
            } else {
            }
        }

        orderDetailRepo.saveAll(orderDetailsList);
        cart.clear();
        return "orderComplete";
    }

}