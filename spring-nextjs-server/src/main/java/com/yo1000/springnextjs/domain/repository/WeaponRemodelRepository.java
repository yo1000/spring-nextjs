package com.yo1000.springnextjs.domain.repository;

import com.yo1000.springnextjs.domain.model.WeaponRemodel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WeaponRemodelRepository {
    Optional<WeaponRemodel> findById(Integer id);
    Page<WeaponRemodel> findAll(Pageable pageable);
}
