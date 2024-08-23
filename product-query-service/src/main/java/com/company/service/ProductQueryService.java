package com.company.service;

import com.company.dto.ProductEvent;
import com.company.model.Product;
import com.company.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @KafkaListener(topics = "product-event-topic", groupId = "product-event-group")
    public void processProductEvents(ProductEvent productEvent) {
        Product product = productEvent.product();
        if (productEvent.eventType().equals("CreateProduct")) {
            productRepository.save(product);
        }
        if (productEvent.eventType().equals("UpdateProduct")) {
            Product existingProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            productRepository.save(existingProduct);
        }
    }
}
