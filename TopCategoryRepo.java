package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TopCategory;

public interface TopCategoryRepo extends JpaRepository<TopCategory, Long> {
}
