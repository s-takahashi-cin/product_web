package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.PositionData;
import com.example.demo.entity.StoreData;
import com.example.demo.entity.UserData;
import com.example.demo.form.EditForm;
import com.example.demo.service.PositionService;
import com.example.demo.service.StoreService;
import com.example.demo.service.UserService;


@Controller
public class AdminListController {

    private final UserService userService;
    private final PositionService positionService;
    private final StoreService storeService;

    @Autowired
    public AdminListController(UserService userService, PositionService positionService, StoreService storeService) {
        this.userService = userService;
        this.positionService = positionService;
        this.storeService = storeService;
    }

    @GetMapping("/admin_list")
    public String adminList(Model model) {
        List<UserData> users = userService.getUserInfoForAdmin();
        model.addAttribute("users", users);
        return "admin_list";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        UserData user = userService.getUserById(id);

        EditForm editForm = new EditForm();
        editForm.setId(user.getId());
        editForm.setLastName(user.getLastName());
        editForm.setFirstName(user.getFirstName());
        editForm.setEmail(user.getEmail());
        editForm.setPhone(user.getPhone());
        editForm.setStoreId(user.getStoreId());
        editForm.setPositionId(user.getPositionId());

        model.addAttribute("editForm", editForm);

        List<PositionData> positions = positionService.getAllPositions();
        model.addAttribute("positions", positions);

        List<StoreData> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);

        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute EditForm editForm, Model model) {
        userService.editUser(id, editForm);
        model.addAttribute("editForm", editForm);
        return "editComplete";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin_list";
    }

}
