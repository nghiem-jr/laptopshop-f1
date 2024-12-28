package com.junior.laptopshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.junior.laptopshop.domain.Product;
import com.junior.laptopshop.service.ProductService;

@Controller
public class HomepageController {
    private final ProductService productService;

    public HomepageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "/client/homepage/show";
    }
}
