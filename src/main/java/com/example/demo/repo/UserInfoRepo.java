package com.example.demo.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserData;

public interface UserInfoRepo extends JpaRepository <UserData, Long> {
	Optional<UserData> findByEmail(String email);
	Optional<UserData> findById(Long id);
	List<UserData> findAllByAuthorityId(Long authorityId);
}
