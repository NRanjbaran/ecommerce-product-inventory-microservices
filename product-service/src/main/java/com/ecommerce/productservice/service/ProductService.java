package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Optional<ProductDto> getProductById(Long id) {
        return null;
    }

    public List<ProductDto> getAllProducts() {
        return null;
    }

    public boolean updateStock(Long id, Integer quantity) {
        return false;
    }
}
