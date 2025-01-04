package com.yo1000.api.presentation;

import com.yo1000.api.application.ItemApplicationService;
import com.yo1000.api.domain.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/items")
@Validated
public class ItemRestController {
    private final ItemApplicationService itemApplicationService;

    public ItemRestController(ItemApplicationService itemApplicationService) {
        this.itemApplicationService = itemApplicationService;
    }

    @GetMapping
    public Page<Item> get(
            @RequestParam(value = "name", required = false)
            String name,
            Pageable pageable
    ) {
        return Optional.ofNullable(name).stream()
                .filter(s -> !s.isBlank())
                .findAny()
                .map(s -> itemApplicationService.search(s, pageable))
                .orElseGet(() -> itemApplicationService.list(pageable));
    }

    @GetMapping("/{id}")
    public Item get(
            @PathVariable("id") Integer id
    ) {
        return itemApplicationService.lookup(id);
    }
}
