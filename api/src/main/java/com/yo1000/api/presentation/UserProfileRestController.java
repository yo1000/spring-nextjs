package com.yo1000.api.presentation;

import com.yo1000.api.application.UserProfileApplicationService;
import com.yo1000.api.domain.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/userProfiles")
public class UserProfileRestController {
    private final UserProfileApplicationService userProfileApplicationService;

    public UserProfileRestController(UserProfileApplicationService userProfileApplicationService) {
        this.userProfileApplicationService = userProfileApplicationService;
    }

    @GetMapping
    public Page<UserProfile> get(
            @RequestParam(value = "username", required = false)
            String name,
            Pageable pageable
    ) {
        return Optional.ofNullable(name).stream()
                .filter(s -> !s.isBlank())
                .findAny()
                .map(s -> userProfileApplicationService.search(s, pageable))
                .orElseGet(() -> userProfileApplicationService.list(pageable));
    }

    @GetMapping("/{id}")
    public UserProfile get(
            @PathVariable("id") Integer id
    ) {
        return userProfileApplicationService.lookup(id);
    }

    @PostMapping
    public UserProfile post(
            @RequestBody UserProfile userProfile
    ) {
        return userProfileApplicationService.create(userProfile);
    }

    @PatchMapping(consumes = "application/merge-patch+json")
    public UserProfile patchByUsername(
            @RequestParam("username") String username,
            @RequestBody String userProfileDiffJson
    ) {
        return userProfileApplicationService.updateDiffByUsername(username, userProfileDiffJson);
    }
}
