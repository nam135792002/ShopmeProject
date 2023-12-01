package com.shopme.admin.test;

import com.shopme.admin.product.ProductService;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestControllerTest {
    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public List<Product> getAllProduct() {
        return productService.listAll();
    }
}
