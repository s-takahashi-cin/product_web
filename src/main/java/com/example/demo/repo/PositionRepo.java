package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.PositionData;

public interface PositionRepo extends JpaRepository<PositionData, Long> {
}