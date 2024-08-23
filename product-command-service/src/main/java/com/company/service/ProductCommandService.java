package com.company.service;

import com.company.dto.ProductEvent;
import com.company.model.Product;
import com.company.repository.ProductRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductCommandService(ProductRepository productRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Product createProduct(ProductEvent productEvent) {
        Product product = productRepository.save(productEvent.product());
        kafkaTemplate.send("product-event-topic", new ProductEvent("CreateProduct", product));
        return product;
    }

    public Product updateProduct(long id, ProductEvent productEvent) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Product newProduct = productEvent.product();
        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setDescription(newProduct.getDescription());

        kafkaTemplate.send("product-event-topic", new ProductEvent("UpdateProduct", productRepository.save(existingProduct)));

        return existingProduct;
    }
}
