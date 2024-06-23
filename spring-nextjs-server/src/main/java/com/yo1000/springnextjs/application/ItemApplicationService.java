package com.yo1000.springnextjs.application;

import com.yo1000.springnextjs.domain.model.Item;
import com.yo1000.springnextjs.domain.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemApplicationService {
    private final ItemRepository itemRepository;

    public ItemApplicationService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> list(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }
}
