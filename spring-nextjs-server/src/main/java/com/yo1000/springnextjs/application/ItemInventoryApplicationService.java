package com.yo1000.springnextjs.application;

import com.yo1000.springnextjs.domain.model.ItemInventory;
import com.yo1000.springnextjs.domain.repository.ItemInventoryRepository;
import com.yo1000.springnextjs.presentation.ItemInventoryPatchRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemInventoryApplicationService {
    private final ItemInventoryRepository itemInventoryRepository;

    public ItemInventoryApplicationService(ItemInventoryRepository itemInventoryRepository) {
        this.itemInventoryRepository = itemInventoryRepository;
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

    public ItemInventory create(ItemInventory itemInventory) {
        if (itemInventoryRepository.findById(itemInventory.getId()).isPresent()) {
            throw new DuplicateKeyException("Entity already exists: " + itemInventory.getId());
        }

        return itemInventoryRepository.save(itemInventory);
    }

    public ItemInventory update(Integer id, ItemInventory itemInventory) {
        ItemInventory existedItemInventory = lookup(id);

        existedItemInventory.setItem(itemInventory.getItem());
        existedItemInventory.setQuantity(itemInventory.getQuantity());

        return itemInventoryRepository.save(existedItemInventory);
    }

    public ItemInventory updateByItemId(Integer itemId, ItemInventory itemInventory) {
        ItemInventory existedItemInventory = lookupByItemId(itemId);

        existedItemInventory.setItem(itemInventory.getItem());
        existedItemInventory.setQuantity(itemInventory.getQuantity());

        return itemInventoryRepository.save(existedItemInventory);
    }

    public ItemInventory updateDiff(Integer id, ItemInventoryPatchRequest itemInventoryPatch) {
        ItemInventory existedItemInventory = lookup(id);

        if (itemInventoryPatch.getItem() != null) {
            itemInventoryPatch.getItem().ifPresentOrElse(
                    existedItemInventory::setItem,
                    () -> existedItemInventory.setItem(null));
        }

        if (itemInventoryPatch.getQuantity() != null) {
            itemInventoryPatch.getQuantity().ifPresentOrElse(
                    existedItemInventory::setQuantity,
                    () -> existedItemInventory.setQuantity(null));
        }

        return itemInventoryRepository.save(existedItemInventory);
    }

    public ItemInventory updateDiffByItemId(Integer itemId, ItemInventoryPatchRequest itemInventoryPatch) {
        ItemInventory existedItemInventory = lookupByItemId(itemId);

        if (itemInventoryPatch.getItem() != null) {
            itemInventoryPatch.getItem().ifPresentOrElse(
                    existedItemInventory::setItem,
                    () -> existedItemInventory.setItem(null));
        }

        if (itemInventoryPatch.getQuantity() != null) {
            itemInventoryPatch.getQuantity().ifPresentOrElse(
                    existedItemInventory::setQuantity,
                    () -> existedItemInventory.setQuantity(null));
        }

        return itemInventoryRepository.save(existedItemInventory);
    }
}
