package com.company.controller;

import com.company.dto.ProductEvent;
import com.company.model.Product;
import com.company.service.ProductCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    private final ProductCommandService productCommandService;

    public ProductCommandController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductEvent productEvent) {
        return productCommandService.createProduct(productEvent);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable long id, @RequestBody ProductEvent productEvent) {
        return productCommandService.updateProduct(id, productEvent);
    }
}

