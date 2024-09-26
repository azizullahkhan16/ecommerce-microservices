package com.aktic.order_service.controller;

import com.aktic.order_service.dto.OrderRequest;
import com.aktic.order_service.dto.OrderResponse;
import com.aktic.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    public ResponseEntity<OrderResponse> placeOrder (@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }

    public ResponseEntity<OrderResponse> fallBackMethod (OrderRequest orderRequest, RuntimeException runtimeException) {
        throw new IllegalArgumentException("Oops! something went wrong");
    }



}
