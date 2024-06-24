package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LowCategory;
import java.util.List;

public interface LowCategoryRepo extends JpaRepository<LowCategory, Long> {
    List<LowCategory> findBySubCategoryId(Long subCategoryId);
}

