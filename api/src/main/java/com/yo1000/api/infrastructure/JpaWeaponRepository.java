package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.Weapon;
import com.yo1000.api.domain.repository.WeaponRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWeaponRepository extends WeaponRepository, JpaRepository<Weapon, Integer> {}
