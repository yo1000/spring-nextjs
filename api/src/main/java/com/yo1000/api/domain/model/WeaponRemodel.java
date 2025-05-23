package com.yo1000.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;
import java.util.Objects;

@Entity
public class WeaponRemodel {
    @Id
    private Integer id;
    @OneToOne
    private Weapon weapon;
    private Integer price;
    @OneToMany//(mappedBy = "weaponRemodel")
    private List<WeaponMaterial> materials;

    public WeaponRemodel() {}

    public WeaponRemodel(Integer id, Weapon weapon, Integer price, List<WeaponMaterial> materials) {
        this.id = id;
        this.weapon = weapon;
        this.price = price;
        this.materials = materials;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<WeaponMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<WeaponMaterial> materials) {
        this.materials = materials;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WeaponRemodel that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(weapon, that.weapon)
                && Objects.equals(price, that.price)
                && Objects.equals(materials, that.materials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weapon, price, materials);
    }
}
