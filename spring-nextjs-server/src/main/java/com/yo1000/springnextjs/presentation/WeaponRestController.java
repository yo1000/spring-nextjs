package com.yo1000.springnextjs.presentation;

import com.yo1000.springnextjs.application.WeaponApplicationService;
import com.yo1000.springnextjs.domain.model.Weapon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/weapons")
public class WeaponRestController {
    private final WeaponApplicationService weaponApplicationService;

    public WeaponRestController(WeaponApplicationService weaponApplicationService) {
        this.weaponApplicationService = weaponApplicationService;
    }

    @GetMapping
    public Page<Weapon> get(
            @RequestParam(value = "name", required = false)
            String name,
            Pageable pageable
    ) {
        return Optional.ofNullable(name).stream()
                .filter(s -> !s.isBlank())
                .findAny()
                .map(s -> weaponApplicationService.search(s, pageable))
                .orElseGet(() -> weaponApplicationService.list(pageable));
    }
}
