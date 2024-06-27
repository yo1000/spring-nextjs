package com.yo1000.springnextjs.presentation;

import com.yo1000.springnextjs.application.WeaponRemodelApplicationService;
import com.yo1000.springnextjs.domain.model.WeaponRemodel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/weaponRemodels")
public class WeaponRemodelRestController {
    private final WeaponRemodelApplicationService weaponRemodelApplicationService;

    public WeaponRemodelRestController(WeaponRemodelApplicationService weaponRemodelApplicationService) {
        this.weaponRemodelApplicationService = weaponRemodelApplicationService;
    }

    @GetMapping
    public Page<WeaponRemodel> get(
            @RequestParam(value = "weaponName", required = false)
            String weaponName,
            Pageable pageable
    ) {
        return Optional.ofNullable(weaponName).stream()
                .filter(s -> !s.isBlank())
                .findAny()
                .map(s -> weaponRemodelApplicationService.search(s, pageable))
                .orElseGet(() -> weaponRemodelApplicationService.list(pageable));
    }
}
