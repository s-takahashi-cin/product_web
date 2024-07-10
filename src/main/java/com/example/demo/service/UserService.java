package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AuthorityData;
import com.example.demo.entity.PositionData;
import com.example.demo.entity.UserData;
import com.example.demo.form.EditForm;
import com.example.demo.form.SignupForm;
import com.example.demo.repo.AuthorityRepo;
import com.example.demo.repo.PositionRepo;
import com.example.demo.repo.UserInfoRepo;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService  {

    private final UserInfoRepo repository;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepo positionRepo;
    private final AuthorityRepo authorityRepo;
    
    

    @Autowired
    public UserService(UserInfoRepo repository, PasswordEncoder passwordEncoder, PositionRepo positionRepo, AuthorityRepo authorityRepo) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.positionRepo = positionRepo;
        this.authorityRepo = authorityRepo;
    }

    public String authenticateUser(String email, String password) {
        Optional<UserData> userInfoOptional = repository.findByEmail(email);
        if (userInfoOptional.isPresent()) {
            UserData userInfo = userInfoOptional.get();
            System.out.println("ユーザーがこちら: " + userInfo.getEmail());
            if (passwordEncoder.matches(password, userInfo.getPassword())) {
                System.out.println("パスワードがマッチ: " + userInfo.getEmail());
                return "Authenticated User";
            } else {
                System.out.println("パスワードが間違ってる: " + userInfo.getEmail());
                return "Incorrect password";
            }
        } else {
            System.out.println("ユーザーが見つからない: " + email);
            return "User not found";
        }
    }

    public String addUser(SignupForm form) {
        Optional<UserData> userInfoExistedOpt = repository.findByEmail(form.getEmail());
        if (userInfoExistedOpt.isPresent()) {
            return "すでに登録されています";
        }

        UserData userData = new UserData();
        userData.setLastName(form.getLastName());
        userData.setFirstName(form.getFirstName());
        userData.setEmail(form.getEmail());
        userData.setPassword(passwordEncoder.encode(form.getPassword()));
        userData.setAuthorityId(form.getAuthorityId());
        userData.setPositionId(form.getPositionId());
        userData.setStoreId(form.getStoreId());

        System.out.println("登録するユーザー情報: " + userData);
        repository.save(userData);
        String message = "ユーザーアカウントが新しく登録されました";
        System.out.println("addUserメソッドのメッセージ: " + message);
        return message;
    }

    public UserData getUserInfo(String email) {
        Optional<UserData> userInfoOptional = repository.findByEmail(email);
        if (userInfoOptional.isPresent()) {
            UserData userInfo = userInfoOptional.get();
            System.out.println("ユーザー情報: " + userInfo.getEmail());
            if (userInfo.getAuthorityId() == 1) {
                return userInfo;
            } else {
                System.out.println("フェッチング: " + userInfo.getId());
                return getUserById(userInfo.getId());
            }
        }
        System.out.println("このメールではユーザー情報が見つかりません: " + email);
        return null;
    }
    
    public List<UserData> getUserInfoForAdmin() {
        List<UserData> users = repository.findAll();
        for (UserData user : users) {
            if (user.getStore() != null) {
                user.setStoreName(user.getStore().getName());
            }
            if (user.getPositionId() != null) {
                Optional<PositionData> positionOptional = positionRepo.findById(user.getPositionId());
                positionOptional.ifPresent(position -> user.setPositionName(position.getName()));
            }
            if (user.getAuthorityId() != null) {
                Optional<AuthorityData> authorityOptional = authorityRepo.findById(user.getAuthorityId());
                authorityOptional.ifPresent(authority -> user.setAuthorityName(authority.getName()));
            }
        }
        return users;
    }
    public UserData getUserById(Long userId) {
        return repository.findById(userId).orElse(null);
    }

    public void editUser(Long id, EditForm editForm) {
        Optional<UserData> userDataOptional = repository.findById(id);
        if (userDataOptional.isPresent()) {
            UserData userData = userDataOptional.get();
            userData.setLastName(editForm.getLastName());
            userData.setFirstName(editForm.getFirstName());
            userData.setEmail(editForm.getEmail());
            userData.setPhone(editForm.getPhone());
            userData.setStoreId(editForm.getStoreId());
            userData.setPositionId(editForm.getPositionId());
            repository.save(userData);
        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}