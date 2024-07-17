package com.example.demo.form;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.OrderDetails;

@Data
@Entity
@Table(name = "orders")
public class OrderForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "quantities")
    private int quantity;

    @Column(name = "total_amount")
    private double totalAmount;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "store_name")
    private String storeName;

    private Double price;
    public Long getId() {
        return id;
    }
    
    @OneToMany(mappedBy = "orderForm", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;

        
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount.doubleValue();
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price.doubleValue();
    }
}
