package com.yo1000.springnextjs.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Weapon {
    @Id
    private Integer id;
    private String name;
    private Integer str;
    private Integer hit;

    public Weapon() {}

    public Weapon(Integer id, String name, Integer str, Integer hit) {
        this.id = id;
        this.name = name;
        this.str = str;
        this.hit = hit;
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

    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }
}
