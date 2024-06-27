package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.repository.ItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaItemRepository extends ItemRepository, JpaRepository<Item, Integer> {}
