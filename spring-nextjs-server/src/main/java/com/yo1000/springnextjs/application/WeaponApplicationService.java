package com.yo1000.springnextjs.application;

import com.yo1000.springnextjs.domain.model.Weapon;
import com.yo1000.springnextjs.domain.repository.WeaponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WeaponApplicationService {
    private final WeaponRepository weaponRepository;

    public WeaponApplicationService(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }

    public Page<Weapon> list(Pageable pageable) {
        return weaponRepository.findAll(pageable);
    }
}
