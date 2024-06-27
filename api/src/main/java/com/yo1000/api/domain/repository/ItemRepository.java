package com.yo1000.api.domain.repository;

import com.yo1000.api.domain.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findById(Integer id);
    Page<Item> findAll(Pageable pageable);
    Page<Item> findAllByNameStartingWith(String name, Pageable pageable);
}
