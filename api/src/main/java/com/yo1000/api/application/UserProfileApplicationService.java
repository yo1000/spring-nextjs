package com.yo1000.api.application;

import com.yo1000.api.domain.model.UserProfile;
import com.yo1000.api.domain.repository.UserProfileRepository;
import com.yo1000.api.presentation.UserProfilePatchRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
//@PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.userProfile:write', 'spring-nextjs.userProfile:read'})")
@PreAuthorize("authenticated")
public class UserProfileApplicationService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileApplicationService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public Page<UserProfile> list(Pageable pageable) {
        return userProfileRepository.findAll(pageable);
    }

    public Page<UserProfile> search(String username, Pageable pageable) {
        return userProfileRepository.findAllByUsernameStartingWith(username, pageable);
    }

    public UserProfile lookup(Integer id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    public UserProfile lookupByUsername(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.userProfile:write'})")
    public UserProfile create(UserProfile userProfile) {
        if (userProfileRepository.findById(userProfile.getId()).isPresent()) {
            throw new DuplicateKeyException("Entity already exists: " + userProfile.getId());
        }

        return userProfileRepository.save(userProfile);
    }

    @PreAuthorize("hasAnyAuthority({'admin'}) || #username == authentication.name")
    public UserProfile updateDiffByUsername(String username, UserProfilePatchRequest userProfilePatchRequest) {
        UserProfile existedUserProfile = lookupByUsername(username);

        userProfilePatchRequest.getId().ifPresent(id -> {
            if (!Objects.equals(existedUserProfile.getId(), id)) {
                throw new InvalidDataAccessResourceUsageException("User id is invalid: " + userProfilePatchRequest.getId());
            }
        });

        if (userProfilePatchRequest.getUsername() != null) {
            userProfilePatchRequest.getUsername()
                    .map(s -> s.isBlank() ? null : s)
                    .ifPresentOrElse(
                            existedUserProfile::setUsername,
                            () -> {
                                throw new IllegalArgumentException("Username can not be null");
                            });
        }

        if (userProfilePatchRequest.getFamilyName() != null) {
            userProfilePatchRequest.getFamilyName().ifPresentOrElse(
                    existedUserProfile::setFamilyName,
                    () -> existedUserProfile.setFamilyName(null));
        }

        if (userProfilePatchRequest.getGivenName() != null) {
            userProfilePatchRequest.getGivenName().ifPresentOrElse(
                    existedUserProfile::setGivenName,
                    () -> existedUserProfile.setGivenName(null));
        }

        if (userProfilePatchRequest.getAge() != null) {
            userProfilePatchRequest.getAge().ifPresentOrElse(
                    existedUserProfile::setAge,
                    () -> existedUserProfile.setAge(null));
        }

        if (userProfilePatchRequest.getGender() != null) {
            userProfilePatchRequest.getGender().ifPresentOrElse(
                    existedUserProfile::setGender,
                    () -> existedUserProfile.setGender(null));
        }

        if (userProfilePatchRequest.getProfile() != null) {
            userProfilePatchRequest.getProfile().ifPresentOrElse(
                    existedUserProfile::setProfile,
                    () -> existedUserProfile.setProfile(null));
        }

        return userProfileRepository.save(existedUserProfile);
    }
}
