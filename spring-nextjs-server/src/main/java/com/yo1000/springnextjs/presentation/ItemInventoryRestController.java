package com.yo1000.springnextjs.presentation;

import com.yo1000.springnextjs.application.ItemInventoryApplicationService;
import com.yo1000.springnextjs.domain.model.ItemInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itemInventories")
public class ItemInventoryRestController {
    private final ItemInventoryApplicationService itemInventoryApplicationService;

    public ItemInventoryRestController(ItemInventoryApplicationService itemInventoryApplicationService) {
        this.itemInventoryApplicationService = itemInventoryApplicationService;
    }

    @GetMapping
    public Page<ItemInventory> get(Pageable pageable) {
        return itemInventoryApplicationService.list(pageable);
    }

    @PostMapping
    public ItemInventory post(
            @RequestBody ItemInventory itemInventory
    ) {
        return itemInventoryApplicationService.create(itemInventory);
    }

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    public ItemInventory patch(
            @PathVariable("id") Integer id,
            @RequestBody ItemInventoryPatchRequest itemInventoryPatchRequest
    ) {
        return itemInventoryApplicationService.updateDiff(id, itemInventoryPatchRequest);
    }

    @PatchMapping(consumes = "application/merge-patch+json")
    public ItemInventory patchByItemId(
            @RequestParam("itemId") Integer itemId,
            @RequestBody ItemInventoryPatchRequest itemInventoryPatchRequest
    ) {
        return itemInventoryApplicationService.updateDiffByItemId(itemId, itemInventoryPatchRequest);
    }
}
