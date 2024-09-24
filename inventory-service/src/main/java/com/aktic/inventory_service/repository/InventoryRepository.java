package com.aktic.inventory_service.repository;

import com.aktic.inventory_service.model.Inventory;
import org.antlr.v4.runtime.atn.RangeTransition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
