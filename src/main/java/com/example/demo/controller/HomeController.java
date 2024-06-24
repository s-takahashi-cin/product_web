package com.example.demo.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.AuthorityData;
import com.example.demo.entity.ManufacturerData;
import com.example.demo.entity.PositionData;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;

import com.example.demo.repo.AuthorityRepo;
import com.example.demo.repo.ManufacturerRepo;
import com.example.demo.repo.PositionRepo;
import com.example.demo.repo.StoreRepo;
import com.example.demo.repo.UserInfoRepo;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Controller
public class HomeController {

	private final UserInfoRepo userInfoRepo;
	private final StoreRepo storeRepo;
	private final ManufacturerRepo manufacturerRepo;
    private final PositionRepo positionRepo;
    private final AuthorityRepo authorityRepo;

	
	public HomeController(UserInfoRepo userInfoRepo, StoreRepo storeRepo, ManufacturerRepo manufacturerRepo, PositionRepo positionRepo, AuthorityRepo authorityRepo) {
		this.userInfoRepo = userInfoRepo;
		this.storeRepo = storeRepo;
		this.manufacturerRepo = manufacturerRepo;
        this.positionRepo = positionRepo;
        this.authorityRepo = authorityRepo;

	}


    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        UserData user = (UserData) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            System.out.println("ログインしているユーザー: " + user.getUsername());
        }
        return "home";
    }

    @GetMapping("/user_info/{id}")
    public String userInfo(@PathVariable Long id, Model model) {
        Optional<UserData> userDataOptional = userInfoRepo.findById(id);
        if (userDataOptional.isPresent()) {
            UserData user = userDataOptional.get();
            if (user.getStoreId() != null) {
                Optional<StoreData> storeOptional = storeRepo.findById(user.getStoreId());
                storeOptional.ifPresent(store -> user.setStoreName(store.getName()));
            }
            if (user.getPositionId() != null) {
                Optional<PositionData> positionOptional = positionRepo.findById(user.getPositionId());
                positionOptional.ifPresent(position -> user.setPositionName(position.getName()));
            }
            if (user.getAuthorityId() != null) {
                Optional<AuthorityData> authorityOptional = authorityRepo.findById(user.getAuthorityId());
                authorityOptional.ifPresent(authority -> user.setAuthorityName(authority.getName()));
            }
            model.addAttribute("user", user);
            return "user_info";
        } else {
            System.err.println("User not found for ID: " + id);
            return "user_not_found";
        }
    }
    

    @GetMapping("/store_info")
    public String stores(Model model) {
        List<StoreData> stores = storeRepo.findAll();
        model.addAttribute("stores", stores);
        return "store_info";
    }



    @GetMapping("/manufacturer")
    public String manufacturers(Model model) {
        List<ManufacturerData> manufacturers = manufacturerRepo.findAll();
        model.addAttribute("manufacturers", manufacturers);
        return "manufacturer";
    }


    @GetMapping("/logout")
    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(null); // 認証情報をクリア
        }
        return "redirect:/login?logout"; // ログアウト後にログインページにリダイレクト
    }
}
