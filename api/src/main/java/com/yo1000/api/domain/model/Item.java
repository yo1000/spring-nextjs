package com.yo1000.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {
    @Id
    private Integer id;
    private String name;
    private Integer price;
    private Integer sellPrice;

    public Item() {}

    public Item(Integer id, String name, Integer price, Integer sellPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sellPrice = sellPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }
}
