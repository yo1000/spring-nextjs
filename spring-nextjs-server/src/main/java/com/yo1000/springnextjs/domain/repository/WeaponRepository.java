package com.yo1000.springnextjs.domain.repository;

import com.yo1000.springnextjs.domain.model.Weapon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WeaponRepository {
    Optional<Weapon> findById(Integer id);
    Page<Weapon> findAll(Pageable pageable);
}
