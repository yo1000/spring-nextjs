package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.UserProfile;
import com.yo1000.api.domain.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

@DataJpaTest
@Testcontainers
public class JpaUserProfileRepositoryTests {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres"));

    @BeforeAll
    static void startContainers() {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());

        // Required to setup DB.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.show-sql", () -> true);
    }

    // When using JPA, the TestEntityManager is used to setup test data.
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaUserProfileRepository repos;

    @Test
    @DisplayName("Given JpaUserProfileRepository, When invoke findAll with PageRequest, Then should return paged content")
    void test_findAll() {
        entityManager.persist(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"));
        entityManager.persist(new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile text 2"));
        entityManager.persist(new UserProfile(9003, "test3", "Family3", "Given3", 23, "Male", "Profile text 3"));
        entityManager.persist(new UserProfile(9004, "test4", "Family4", "Given4", 24, "Female", "Profile text 4"));

        Page<UserProfile> pagedEntity = repos.findAll(PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"),
                new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile text 2"),
                new UserProfile(9003, "test3", "Family3", "Given3", 23, "Male", "Profile text 3")
        );
    }

    @Test
    @DisplayName("Given JpaUserProfileRepository, When invoke findAllByUsernameStartingWith with username, Then should return left-hand match filtered content")
    void test_findAllByUsernameStartingWith() {
        entityManager.persist(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"));
        entityManager.persist(new UserProfile(9002, "demo2", "Family2", "Given2", 22, "Female", "Profile text 2"));
        entityManager.persist(new UserProfile(9003, "example3", "Family3", "Given3", 23, "Male", "Profile text 3"));
        entityManager.persist(new UserProfile(9004, "testing4", "Family4", "Given4", 24, "Female", "Profile text 4"));

        Page<UserProfile> pagedEntity = repos.findAllByUsernameStartingWith("test", PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"),
                new UserProfile(9004, "testing4", "Family4", "Given4", 24, "Female", "Profile text 4")
        );
    }

    @Test
    @DisplayName("Given JpaUserProfileRepository, When invoke findById with id, Then should return matched content")
    void test_findById() {
        entityManager.persist(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"));
        entityManager.persist(new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile text 2"));
        entityManager.persist(new UserProfile(9003, "test3", "Family3", "Given3", 23, "Male", "Profile text 3"));
        entityManager.persist(new UserProfile(9004, "test4", "Family4", "Given4", 24, "Female", "Profile text 4"));

        Optional<UserProfile> optionalEntity = ((UserProfileRepository) repos).findById(9003);

        Assertions.assertThat(optionalEntity.isPresent()).isTrue();
        optionalEntity.ifPresent(entity -> {
            Assertions.assertThat(entity.getId()).isEqualTo(9003);
            Assertions.assertThat(entity.getUsername()).isEqualTo("test3");
            Assertions.assertThat(entity.getFamilyName()).isEqualTo("Family3");
            Assertions.assertThat(entity.getGivenName()).isEqualTo("Given3");
            Assertions.assertThat(entity.getAge()).isEqualTo(23);
            Assertions.assertThat(entity.getGender()).isEqualTo("Male");
            Assertions.assertThat(entity.getProfile()).isEqualTo("Profile text 3");
        });
    }

    @Test
    @DisplayName("Given JpaUserProfileRepository, When invoke findByUsername with itemId, Then should return matched content")
    void test_findByUsername() {
        entityManager.persist(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"));
        entityManager.persist(new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile text 2"));
        entityManager.persist(new UserProfile(9003, "test3", "Family3", "Given3", 23, "Male", "Profile text 3"));
        entityManager.persist(new UserProfile(9004, "test4", "Family4", "Given4", 24, "Female", "Profile text 4"));

        Optional<UserProfile> optionalEntity = repos.findByUsername("test2");

        Assertions.assertThat(optionalEntity.isPresent()).isTrue();
        optionalEntity.ifPresent(entity -> {
            Assertions.assertThat(entity.getId()).isEqualTo(9002);
            Assertions.assertThat(entity.getUsername()).isEqualTo("test2");
            Assertions.assertThat(entity.getFamilyName()).isEqualTo("Family2");
            Assertions.assertThat(entity.getGivenName()).isEqualTo("Given2");
            Assertions.assertThat(entity.getAge()).isEqualTo(22);
            Assertions.assertThat(entity.getGender()).isEqualTo("Female");
            Assertions.assertThat(entity.getProfile()).isEqualTo("Profile text 2");
        });
    }

    @Test
    @DisplayName("Given JpaUserProfileRepository, When invoke save, Then should store content")
    void test_save() {
        entityManager.persist(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile text 1"));
        entityManager.persist(new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile text 2"));
        entityManager.persist(new UserProfile(9003, "test3", "Family3", "Given3", 23, "Male", "Profile text 3"));
        entityManager.persist(new UserProfile(9004, "test4", "Family4", "Given4", 24, "Female", "Profile text 4"));

        UserProfile savedEntity = ((UserProfileRepository) repos).save(new UserProfile(
                9005, "test5", "Family5", "Given5", 25, "Male", "Profile text 5"));

        Assertions.assertThat(savedEntity.getId()).isEqualTo(9005);
        Assertions.assertThat(savedEntity.getUsername()).isEqualTo("test5");
        Assertions.assertThat(savedEntity.getFamilyName()).isEqualTo("Family5");
        Assertions.assertThat(savedEntity.getGivenName()).isEqualTo("Given5");
        Assertions.assertThat(savedEntity.getAge()).isEqualTo(25);
        Assertions.assertThat(savedEntity.getGender()).isEqualTo("Male");
        Assertions.assertThat(savedEntity.getProfile()).isEqualTo("Profile text 5");
    }
}
