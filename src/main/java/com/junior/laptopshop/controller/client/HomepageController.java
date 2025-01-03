package com.junior.laptopshop.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.junior.laptopshop.domain.Product;
import com.junior.laptopshop.domain.User;
import com.junior.laptopshop.domain.dto.RegisterDTO;
import com.junior.laptopshop.service.ProductService;
import com.junior.laptopshop.service.UserService;

import jakarta.validation.Valid;

@Controller
public class HomepageController {
    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomepageController(ProductService productService, UserService userService,
            PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "/client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handelRegister(@ModelAttribute("registerUser") @Valid RegisterDTO junior,
            BindingResult registerDTOBindingResult) {

        List<FieldError> errors = registerDTOBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        if (registerDTOBindingResult.hasErrors()) {
            return "client/auth/register";
        }

        User user = this.userService.registerDTOtoUser(junior);
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName("USER"));
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "client/auth/login";
    }

}
