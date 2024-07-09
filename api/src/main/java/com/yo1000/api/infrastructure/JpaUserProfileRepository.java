package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.UserProfile;
import com.yo1000.api.domain.repository.UserProfileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserProfileRepository extends UserProfileRepository, JpaRepository<UserProfile, Integer> {}
