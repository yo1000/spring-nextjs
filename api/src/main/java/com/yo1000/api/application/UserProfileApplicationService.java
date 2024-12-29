package com.yo1000.api.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.yo1000.api.application.json.ValidatableObjectReader;
import com.yo1000.api.domain.model.UserProfile;
import com.yo1000.api.domain.repository.UserProfileRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
//@PreAuthorize("hasAnyAuthority({'admin', 'spring-nextjs.userProfile:write', 'spring-nextjs.userProfile:read'})")
@PreAuthorize("authenticated")
public class UserProfileApplicationService {
    private final UserProfileRepository userProfileRepository;
    private final ObjectMapper objectMapper;

    public UserProfileApplicationService(UserProfileRepository userProfileRepository, ObjectMapper objectMapper) {
        this.userProfileRepository = userProfileRepository;
        this.objectMapper = objectMapper;
    }

    public Page<UserProfile> list(Pageable pageable) {;
        return userProfileRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))));
    }

    public Page<UserProfile> search(String username, Pageable pageable) {
        return userProfileRepository.findAllByUsernameStartingWith(username, PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))));
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

    // authentication: UserProfiledAuthenticationToken
    // principal: UserProfile
    @PreAuthorize("hasAnyAuthority({'admin'}) || principal.id == 1 || #username == authentication.name")
    public UserProfile updateDiffByUsername(String username, String userProfileDiffJson) {
        UserProfile existedUserProfile = lookupByUsername(username);

        try {
            ObjectReader reader = new ValidatableObjectReader(objectMapper.readerForUpdating(existedUserProfile));
            UserProfile updateUserProfile = reader.readValue(userProfileDiffJson);

            if (!Objects.equals(existedUserProfile.getId(), updateUserProfile.getId())) {
                throw new InvalidDataAccessResourceUsageException("User id is invalid: " + updateUserProfile.getId());
            }

            if (updateUserProfile.getUsername() == null) {
                throw new IllegalArgumentException("Username can not be null");
            }

            return userProfileRepository.save(existedUserProfile);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
