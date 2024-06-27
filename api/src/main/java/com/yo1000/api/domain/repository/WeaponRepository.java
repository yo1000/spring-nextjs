package com.yo1000.api.domain.repository;

import com.yo1000.api.domain.model.Weapon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WeaponRepository {
    Optional<Weapon> findById(Integer id);
    Page<Weapon> findAll(Pageable pageable);
    Page<Weapon> findAllByNameStartingWith(String name, Pageable pageable);
}
