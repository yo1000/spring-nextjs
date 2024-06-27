package com.yo1000.api.application;

import com.yo1000.api.domain.model.Weapon;
import com.yo1000.api.domain.repository.WeaponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.weapon:write', 'spring-nextjs.weapon:read'})")
public class WeaponApplicationService {
    private final WeaponRepository weaponRepository;

    public WeaponApplicationService(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }

    public Page<Weapon> list(Pageable pageable) {
        return weaponRepository.findAll(pageable);
    }

    public Page<Weapon> search(String name, Pageable pageable) {
        return weaponRepository.findAllByNameStartingWith(name, pageable);
    }
}
