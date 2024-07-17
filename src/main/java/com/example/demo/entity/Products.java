package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_category_id")
    private Long productCategoryId;

    @JoinColumn(name = "store_id")
    private Long storeId;

    @JoinColumn(name = "name")
    private String name;

    private String body;
    private String image;
    private double price;

    @JoinColumn(name = "retail_price")
    private double retailPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    private List<ProductStorePrice> storePrices;

    @ManyToOne
    @JoinColumn(name = "low_category_id")
    private LowCategory lowCategory;
}
