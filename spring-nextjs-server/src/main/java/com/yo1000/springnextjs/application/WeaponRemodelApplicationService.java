package com.yo1000.springnextjs.application;

import com.yo1000.springnextjs.domain.model.WeaponRemodel;
import com.yo1000.springnextjs.domain.repository.WeaponRemodelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WeaponRemodelApplicationService {
    private final WeaponRemodelRepository weaponRemodelRepository;

    public WeaponRemodelApplicationService(WeaponRemodelRepository weaponRemodelRepository) {
        this.weaponRemodelRepository = weaponRemodelRepository;
    }

    public Page<WeaponRemodel> list(Pageable pageable) {
        return weaponRemodelRepository.findAll(pageable);
    }
}
