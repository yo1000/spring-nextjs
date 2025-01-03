package com.yo1000.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yo1000.api.domain.model.UserProfile;
import com.yo1000.api.domain.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

public class UserProfileApplicationServiceTests {
    @Test
    void test_list() {
        // Arrange
        UserProfileRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new UserProfile(9001, "test1", "Family1", "Given1", 11, "Male", "Profile1"),
                                new UserProfile(9002, "test2", "Family2", "Given2", 12, "Female", "Profile2"),
                                new UserProfile(9003, "test3", "Family3", "Given3", 13, "Male", "Profile3")),
                        PageRequest.of(0, 3),
                        4))
                .when(mockedRepos)
                .findAll(Mockito.any());

        ObjectMapper objectMapper = new ObjectMapper();

        UserProfileApplicationService service = new UserProfileApplicationService(mockedRepos, objectMapper);

        // Act
        Page<UserProfile> pagedEntity = service.list(PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).usingRecursiveFieldByFieldElementComparator().contains(
                new UserProfile(9001, "test1", "Family1", "Given1", 11, "Male", "Profile1"),
                new UserProfile(9002, "test2", "Family2", "Given2", 12, "Female", "Profile2"),
                new UserProfile(9003, "test3", "Family3", "Given3", 13, "Male", "Profile3"));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void test_search() {
        // Arrange
        UserProfileRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new UserProfile(9001, "test1", "Family1", "Given1", 11, "Male", "Profile1"),
                                new UserProfile(9004, "testing4", "Family4", "Given4", 14, "Female", "Profile4")),
                        PageRequest.of(0, 3),
                        4))
                .when(mockedRepos)
                .findAllByUsernameStartingWith(Mockito.anyString(), Mockito.any(Pageable.class));

        ObjectMapper objectMapper = new ObjectMapper();

        UserProfileApplicationService service = new UserProfileApplicationService(mockedRepos, objectMapper);

        // Act
        Page<UserProfile> pagedEntity = service.search("test", PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).usingRecursiveFieldByFieldElementComparator().contains(
                new UserProfile(9001, "test1", "Family1", "Given1", 11, "Male", "Profile1"),
                new UserProfile(9004, "testing4", "Family4", "Given4", 14, "Female", "Profile4"));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAllByUsernameStartingWith("test", PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void test_lookup() {
        // Arrange
        UserProfileRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new UserProfile(9002, "test2", "Family2", "Given2", 12, "Female", "Profile2")))
                .when(mockedRepos)
                .findById(Mockito.any());

        ObjectMapper objectMapper = new ObjectMapper();

        UserProfileApplicationService service = new UserProfileApplicationService(mockedRepos, objectMapper);

        // Act
        UserProfile entity = service.lookup(9002);

        // Assert
        Assertions.assertThat(entity).isEqualTo(new UserProfile(9002, "test2", "Family2", "Given2", 12, "Female", "Profile2"));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(9002);
    }

    @Test
    void test_lookupByUsername() {
        // Arrange
        UserProfileRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new UserProfile(9002, "test2", "Family2", "Given2", 12, "Female", "Profile2")))
                .when(mockedRepos)
                .findByUsername(Mockito.any());

        ObjectMapper objectMapper = new ObjectMapper();

        UserProfileApplicationService service = new UserProfileApplicationService(mockedRepos, objectMapper);

        // Act
        UserProfile entity = service.lookupByUsername("test2");

        // Assert
        Assertions.assertThat(entity).isEqualTo(new UserProfile(9002, "test2", "Family2", "Given2", 12, "Female", "Profile2"));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findByUsername("test2");
    }

    @Test
    void test_create() {
        // Arrange
        UserProfileRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.empty())
                .when(mockedRepos)
                .findById(Mockito.any());
        Mockito.doReturn(new UserProfile(9003, "test3", "Family3", "Given3", 13, "Male", "Profile3"))
                .when(mockedRepos)
                .save(Mockito.any(UserProfile.class));

        ObjectMapper objectMapper = new ObjectMapper();

        UserProfileApplicationService service = new UserProfileApplicationService(mockedRepos, objectMapper);

        // Act
        UserProfile entity = service.create(new UserProfile(null, "test3", "Family3", "Given3", 13, "Male", "Profile3"));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new UserProfile(9003, "test3", "Family3", "Given3", 13, "Male", "Profile3"));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(null);
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new UserProfile(null, "test3", "Family3", "Given3", 13, "Male", "Profile3"));
    }

    @Test
    void test_updateDiffByUsername() throws Throwable {
        // Arrange
        UserProfileRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new UserProfile(9003, "test3", "Family3", "Given3", 13, "Male", "Profile3")))
                .when(mockedRepos)
                .findByUsername(Mockito.any());
        Mockito.doReturn(
                        new UserProfile(9003, "test3", "Family33", "Given33", 33, "Female", "Profile33"))
                .when(mockedRepos)
                .save(Mockito.any(UserProfile.class));

        ObjectMapper objectMapper = new ObjectMapper();

        UserProfileApplicationService service = new UserProfileApplicationService(mockedRepos, objectMapper);

        // Act
        UserProfile entity = service.updateDiffByUsername("test3", objectMapper.writeValueAsString(new UserProfile(9003, "test3", "Family33", "Given33", 33, "Female", "Profile33")));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new UserProfile(9003, "test3", "Family33", "Given33", 33, "Female", "Profile33"));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findByUsername("test3");
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new UserProfile(9003, "test3", "Family33", "Given33", 33, "Female", "Profile33"));
    }
}
