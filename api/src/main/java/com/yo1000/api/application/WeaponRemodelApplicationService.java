package com.yo1000.api.application;

import com.yo1000.api.domain.model.WeaponRemodel;
import com.yo1000.api.domain.repository.WeaponRemodelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.weaponRemodel:write', 'spring-nextjs.weaponRemodel:read'})")
public class WeaponRemodelApplicationService {
    private final WeaponRemodelRepository weaponRemodelRepository;

    public WeaponRemodelApplicationService(WeaponRemodelRepository weaponRemodelRepository) {
        this.weaponRemodelRepository = weaponRemodelRepository;
    }

    public Page<WeaponRemodel> list(Pageable pageable) {
        return weaponRemodelRepository.findAll(pageable);
    }

    public Page<WeaponRemodel> search(String weaponName, Pageable pageable) {
        return weaponRemodelRepository.findAllByWeaponNameStartingWith(weaponName, pageable);
    }
}
