package com.yo1000.api.domain.repository;

import com.yo1000.api.domain.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserProfileRepository {
    Optional<UserProfile> findById(Integer id);
    Optional<UserProfile> findByUsername(String username);
    Page<UserProfile> findAll(Pageable pageable);
    Page<UserProfile> findAllByUsernameStartingWith(String username, Pageable pageable);
    UserProfile save(UserProfile userProfile);
}
