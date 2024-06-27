package com.yo1000.api.presentation;

import com.yo1000.api.domain.model.Item;

import java.util.Optional;

public class ItemInventoryPatchRequest {
    private Optional<Item> item;
    private Optional<Integer> quantity;

    public ItemInventoryPatchRequest() {}

    public ItemInventoryPatchRequest(Optional<Item> item, Optional<Integer> quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Optional<Item> getItem() {
        return item;
    }

    public void setItem(Optional<Item> item) {
        this.item = item;
    }

    public Optional<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(Optional<Integer> quantity) {
        this.quantity = quantity;
    }
}
