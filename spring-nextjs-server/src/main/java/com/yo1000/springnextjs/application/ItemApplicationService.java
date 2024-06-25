package com.yo1000.springnextjs.application;

import com.yo1000.springnextjs.domain.model.Item;
import com.yo1000.springnextjs.domain.repository.ItemRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.item:write', 'spring-nextjs.item:read'})")
public class ItemApplicationService {
    private final ItemRepository itemRepository;

    public ItemApplicationService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> list(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Page<Item> search(String name, Pageable pageable) {
        return itemRepository.findAllByNameStartingWith(name, pageable);
    }

    public Item lookup(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}
