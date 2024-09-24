package com.aktic.product_service.service;

import com.aktic.product_service.dto.ProductRequest;
import com.aktic.product_service.dto.ProductResponse;
import com.aktic.product_service.model.Product;
import com.aktic.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private ProductResponse mapToProductResponse (Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public ResponseEntity<ProductResponse> createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        product = productRepository.save(product);
        ProductResponse productResponse = mapToProductResponse(product);

        log.info("Product {} created", productResponse.getId());
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);

    }

    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return new ResponseEntity<>(products.stream().map(this::mapToProductResponse).toList(), HttpStatus.OK);
    }
}
