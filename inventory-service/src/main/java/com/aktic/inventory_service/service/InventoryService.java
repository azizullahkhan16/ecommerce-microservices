package com.aktic.inventory_service.service;

import com.aktic.inventory_service.dto.InventoryResponse;
import com.aktic.inventory_service.model.Inventory;
import com.aktic.inventory_service.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    public ResponseEntity<List<InventoryResponse>> isInStock(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        return new ResponseEntity<>(inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream().map(inventory ->
                        InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build()).toList(), HttpStatus.OK);
    }
}
