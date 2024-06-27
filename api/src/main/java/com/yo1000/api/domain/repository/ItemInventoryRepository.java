package com.yo1000.api.domain.repository;

import com.yo1000.api.domain.model.ItemInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemInventoryRepository {
    Optional<ItemInventory> findById(Integer id);
    Page<ItemInventory> findAll(Pageable pageable);
    ItemInventory save(ItemInventory itemInventory);

    Optional<ItemInventory> findByItemId(Integer id);
    Page<ItemInventory> findAllByItemNameStartingWith(String name, Pageable pageable);
}
