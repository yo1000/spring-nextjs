package com.yo1000.springnextjs.infrastructure;

import com.yo1000.springnextjs.domain.model.Weapon;
import com.yo1000.springnextjs.domain.repository.WeaponRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWeaponRepository extends WeaponRepository, JpaRepository<Weapon, Integer> {}
