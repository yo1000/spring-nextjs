package com.yo1000.springnextjs.infrastructure;

import com.yo1000.springnextjs.domain.model.Item;
import com.yo1000.springnextjs.domain.repository.ItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaItemRepository extends ItemRepository, JpaRepository<Item, Integer> {}
