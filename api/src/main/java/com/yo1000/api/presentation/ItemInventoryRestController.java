package com.yo1000.api.presentation;

import com.yo1000.api.application.ItemInventoryApplicationService;
import com.yo1000.api.domain.model.ItemInventory;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/itemInventories")
@Validated
public class ItemInventoryRestController {
    private final ItemInventoryApplicationService itemInventoryApplicationService;

    public ItemInventoryRestController(ItemInventoryApplicationService itemInventoryApplicationService) {
        this.itemInventoryApplicationService = itemInventoryApplicationService;
    }

    @GetMapping
    public Page<ItemInventory> get(
            @RequestParam(value = "itemName", required = false)
            String itemName,
            Pageable pageable
    ) {
        return Optional.ofNullable(itemName).stream()
                .filter(s -> !s.isBlank())
                .findAny()
                .map(s -> itemInventoryApplicationService.search(s, pageable))
                .orElseGet(() -> itemInventoryApplicationService.list(pageable));
    }

    @PostMapping
    public ItemInventory post(
            @RequestBody @Valid ItemInventory itemInventory
    ) {
        return itemInventoryApplicationService.create(itemInventory);
    }

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    public ItemInventory patch(
            @PathVariable("id") Integer id,
            @RequestBody String itemInventoryDiffJson
    ) {
        return itemInventoryApplicationService.updateDiff(id, itemInventoryDiffJson);
    }

    @PatchMapping(consumes = "application/merge-patch+json")
    public ItemInventory patchByItemId(
            @RequestParam("itemId") Integer itemId,
            @RequestBody String itemInventoryDiffJson
    ) {
        return itemInventoryApplicationService.updateDiffByItemId(itemId, itemInventoryDiffJson);
    }
}
