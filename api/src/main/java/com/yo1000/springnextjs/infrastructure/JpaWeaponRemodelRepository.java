package com.yo1000.springnextjs.infrastructure;

import com.yo1000.springnextjs.domain.model.WeaponRemodel;
import com.yo1000.springnextjs.domain.repository.WeaponRemodelRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWeaponRemodelRepository extends WeaponRemodelRepository, JpaRepository<WeaponRemodel, Integer> {}
