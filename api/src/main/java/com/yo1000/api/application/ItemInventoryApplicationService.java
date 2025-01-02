package com.yo1000.api.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.yo1000.api.application.json.ValidatableObjectReader;
import com.yo1000.api.domain.model.ItemInventory;
import com.yo1000.api.domain.repository.ItemInventoryRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.itemInventory:write', 'spring-nextjs.itemInventory:read'})")
public class ItemInventoryApplicationService {
    private final ItemInventoryRepository itemInventoryRepository;
    private final ObjectMapper objectMapper;

    public ItemInventoryApplicationService(ItemInventoryRepository itemInventoryRepository, ObjectMapper objectMapper) {
        this.itemInventoryRepository = itemInventoryRepository;
        this.objectMapper = objectMapper;
    }

    public Page<ItemInventory> list(Pageable pageable) {
        return itemInventoryRepository.findAll(pageable);
    }

    public ItemInventory lookup(Integer id) {
        return itemInventoryRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public ItemInventory lookupByItemId(Integer itemId) {
        return itemInventoryRepository.findByItemId(itemId)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public Page<ItemInventory> search(String itemName, Pageable pageable) {
        return itemInventoryRepository.findAllByItemNameStartingWith(itemName, pageable);
    }

    @PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.itemInventory:write'})")
    public ItemInventory create(ItemInventory itemInventory) {
        if (itemInventoryRepository.findById(itemInventory.getId()).isPresent()) {
            throw new DuplicateKeyException("Entity already exists: " + itemInventory.getId());
        }

        return itemInventoryRepository.save(itemInventory);
    }

    @PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.itemInventory:write'})")
    public ItemInventory update(Integer id, ItemInventory itemInventory) {
        ItemInventory existedItemInventory = lookup(id);

        existedItemInventory.setItem(itemInventory.getItem());
        existedItemInventory.setQuantity(itemInventory.getQuantity());

        return itemInventoryRepository.save(existedItemInventory);
    }

    @PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.itemInventory:write'})")
    public ItemInventory updateByItemId(Integer itemId, ItemInventory itemInventory) {
        ItemInventory existedItemInventory = lookupByItemId(itemId);

        existedItemInventory.setItem(itemInventory.getItem());
        existedItemInventory.setQuantity(itemInventory.getQuantity());

        return itemInventoryRepository.save(existedItemInventory);
    }

    @PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.itemInventory:write'})")
    public ItemInventory updateDiff(Integer id, String itemInventoryDiffJson) {
        ItemInventory existedItemInventory = lookup(id);

        try {
            ObjectReader reader = new ValidatableObjectReader(objectMapper.readerForUpdating(existedItemInventory));
            ItemInventory updateItemInventory = reader.readValue(itemInventoryDiffJson);

            if (!Objects.equals(existedItemInventory.getId(), updateItemInventory.getId())) {
                throw new InvalidDataAccessResourceUsageException("ItemInventory id is invalid: " + updateItemInventory.getId());
            }

            if (updateItemInventory.getItem() == null) {
                throw new IllegalArgumentException("itemId can not be null");
            }

            return itemInventoryRepository.save(existedItemInventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.itemInventory:write'})")
    public ItemInventory updateDiffByItemId(Integer itemId, String itemInventoryDiffJson) {
        ItemInventory existedItemInventory = lookupByItemId(itemId);

        try {
            ObjectReader reader = new ValidatableObjectReader(objectMapper.readerForUpdating(existedItemInventory));
            ItemInventory updateItemInventory = reader.readValue(itemInventoryDiffJson);

            if (!Objects.equals(existedItemInventory.getId(), updateItemInventory.getId())) {
                throw new InvalidDataAccessResourceUsageException("ItemInventory id is invalid: " + updateItemInventory.getId());
            }

            if (updateItemInventory.getItem() == null) {
                throw new IllegalArgumentException("itemId can not be null");
            }

            return itemInventoryRepository.save(existedItemInventory);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
