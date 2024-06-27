package com.yo1000.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class WeaponMaterial {
    @Id
    private Integer id;
    @ManyToOne
    private Weapon weapon;
    @ManyToOne
    private Item item;

    private Integer quantity;

    public WeaponMaterial() {}

    public WeaponMaterial(Integer id, Weapon weapon, Item item, Integer quantity) {
        this.id = id;
        this.weapon = weapon;
        this.item = item;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
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
}
