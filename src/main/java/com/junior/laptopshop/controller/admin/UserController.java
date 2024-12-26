package com.junior.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.junior.laptopshop.domain.User;
import com.junior.laptopshop.service.UploadService;
import com.junior.laptopshop.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private UserService userService;
    private UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder, UserService userService, UploadService uploadService) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUsersByEmail("nghiem@gmail.com");
        System.out.println(arrUsers);
        model.addAttribute("variable", "value");
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @RequestMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String postUserPage(Model model,
            @ModelAttribute("newUser") @Valid User junior,
            BindingResult newUserBindingResult,
            @RequestParam("juniorFile") MultipartFile file) {

        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        // validate
        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }

        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(junior.getPassword());
        junior.setAvatar(avatar);
        junior.setPassword(hashPassword);
        junior.setRole(this.userService.getRoleByName(junior.getRole().getName()));

        // save
        this.userService.handleSaveUser(junior);
        return "redirect:/admin/user";
    }

    @RequestMapping("/admin/user/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User junior) {
        User currentUser = this.userService.getUserById(junior.getId());
        if (currentUser != null) {
            currentUser.setFullName(junior.getFullName());
            currentUser.setPhone(junior.getPhone());
            currentUser.setAddress(junior.getAddress());

            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User junior) {
        User currentUser = this.userService.getUserById(junior.getId());
        this.userService.deleteUserById(currentUser.getId());
        return "redirect:/admin/user";
    }
}
