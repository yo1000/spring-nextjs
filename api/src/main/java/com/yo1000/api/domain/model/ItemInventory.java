package com.yo1000.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
public class ItemInventory {
    @Id
    private Integer id;
    @OneToOne
    private Item item;
    private Integer quantity;

    public ItemInventory() {}

    public ItemInventory(Integer id, Item item, Integer quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
    }

    public void receive(int quantity) {
        setQuantity(getQuantity() + quantity);
    }

    public void use(int quantity) {
        if (getQuantity() - quantity < 0) {
            throw new IllegalArgumentException("Inventory shortages");
        }

        setQuantity(getQuantity() - quantity);
    }

    public void remove() {
        setQuantity(0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemInventory that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(item, that.item)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, quantity);
    }
}
