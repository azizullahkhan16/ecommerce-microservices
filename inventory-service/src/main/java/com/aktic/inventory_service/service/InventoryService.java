package com.aktic.inventory_service.service;

import com.aktic.inventory_service.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    public ResponseEntity<Boolean> isInStock(String skuCode) {
        return new ResponseEntity<>(inventoryRepository.findBySkuCode(skuCode).isPresent(), HttpStatus.OK);
    }
}
