package com.example.demo.controller;

import com.example.demo.entity.UserData;
import com.example.demo.form.LoginForm;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/signin")
    public String showSigninForm(Model model) {
        // System.out.println("サインインする");
        model.addAttribute("loginForm", new LoginForm());
        return "signin";
    }

    @PostMapping("/home")
    public String signin(@ModelAttribute LoginForm form, RedirectAttributes redirectAttributes, HttpSession session) {
        String email = form.getEmail();
        String password = form.getPassword();
        System.out.println("ログイン用のメールア11ドレス: " + email);
        String message = userService.authenticateUser(email, password);
        if (message.equals("Authenticated User")) {
            UserData user = userService.getUserInfo(email);
            session.setAttribute("user", user);
            redirectAttributes.addFlashAttribute("successMessage", "Logged in successfully!"); 
            return "redirect:/home"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/signin";
        }
    }
}
