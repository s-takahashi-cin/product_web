package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.MidCategory;

import java.util.List;

public interface MidCategoryRepo extends JpaRepository<MidCategory, Long> {
    List<MidCategory> findByTopCategoryId(Long topCategoryId);
}
