package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.ItemInventory;
import com.yo1000.api.domain.repository.ItemInventoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaItemInventoryRepository extends ItemInventoryRepository, JpaRepository<ItemInventory, Integer> {}
