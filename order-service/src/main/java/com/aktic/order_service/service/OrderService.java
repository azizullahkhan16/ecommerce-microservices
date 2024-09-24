package com.aktic.order_service.service;

import com.aktic.order_service.dto.InventoryResponse;
import com.aktic.order_service.dto.OrderLineItemsDto;
import com.aktic.order_service.dto.OrderRequest;
import com.aktic.order_service.dto.OrderResponse;
import com.aktic.order_service.model.Order;
import com.aktic.order_service.model.OrderLineItems;
import com.aktic.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public ResponseEntity<OrderResponse> placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToOrderLineItems).toList();

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = orderLineItemsList.stream().map(OrderLineItems::getSkuCode).toList();

        // Calling inventory service to provide availability of the order line items
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);

        if(allProductsInStock) {
            Order savedOrder = orderRepository.save(order);

            log.info("Order {} created", savedOrder.getId());
            return new ResponseEntity<>(mapToOrderResponse(savedOrder), HttpStatus.CREATED);
        } else {
            throw new IllegalArgumentException("Product is not in stock");
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .id(order.getId())
                .orderLineItemsList(order.getOrderLineItemsList())
                .build();
    }


    private OrderLineItems mapToOrderLineItems (OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }
}
