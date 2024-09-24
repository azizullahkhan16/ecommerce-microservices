package com.aktic.order_service.service;

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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public ResponseEntity<OrderResponse> placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToOrderLineItems).toList();

        order.setOrderLineItemsList(orderLineItemsList);

        Order savedOrder = orderRepository.save(order);

        log.info("Order {} created", savedOrder.getId());
        return new ResponseEntity<>(mapToOrderResponse(savedOrder), HttpStatus.CREATED);
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
