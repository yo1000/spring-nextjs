package com.yo1000.springnextjs.presentation;

import com.yo1000.springnextjs.application.WeaponRemodelApplicationService;
import com.yo1000.springnextjs.domain.model.WeaponRemodel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weaponRemodels")
public class WeaponRemodelRestController {
    private final WeaponRemodelApplicationService weaponRemodelApplicationService;

    public WeaponRemodelRestController(WeaponRemodelApplicationService weaponRemodelApplicationService) {
        this.weaponRemodelApplicationService = weaponRemodelApplicationService;
    }

    @GetMapping
    public Page<WeaponRemodel> get(Pageable pageable) {
        return weaponRemodelApplicationService.list(pageable);
    }
}
