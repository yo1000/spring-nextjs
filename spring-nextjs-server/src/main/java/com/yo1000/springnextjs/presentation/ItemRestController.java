package com.yo1000.springnextjs.presentation;

import com.yo1000.springnextjs.application.ItemApplicationService;
import com.yo1000.springnextjs.domain.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemRestController {
    private final ItemApplicationService itemApplicationService;

    public ItemRestController(ItemApplicationService itemApplicationService) {
        this.itemApplicationService = itemApplicationService;
    }

    @GetMapping
    public Page<Item> get(Pageable pageable) {
        return itemApplicationService.list(pageable);
    }
}
