package com.company.dto;

import com.company.model.Product;

public record ProductEvent(
        String eventType,
        Product product
) {
}
