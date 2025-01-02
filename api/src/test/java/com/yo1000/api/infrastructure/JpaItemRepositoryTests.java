package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.repository.ItemRepository;
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
public class JpaItemRepositoryTests {
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
    JpaItemRepository repos;

    @Test
    @DisplayName("Given JpaItemRepository, When invoke findAll with PageRequest, Then should return paged content")
    void test_findAll() {
        entityManager.persist(new Item(9001, "Test1", 100, 50));
        entityManager.persist(new Item(9002, "Test2", 200, 100));
        entityManager.persist(new Item(9003, "Test3", 300, 150));
        entityManager.persist(new Item(9004, "Test4", 400, 200));

        Page<Item> pagedEntity = repos.findAll(PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Item(9001, "Test1", 100, 50),
                new Item(9002, "Test2", 200, 100),
                new Item(9003, "Test3", 300, 150)
        );
    }

    @Test
    @DisplayName("Given JpaItemRepository, When invoke findAllByNameStartingWith with name, Then should return left-hand match filtered content")
    void test_findAllByNameStartingWith() {
        entityManager.persist(new Item(9001, "Test1", 100, 50));
        entityManager.persist(new Item(9002, "Demo2", 200, 100));
        entityManager.persist(new Item(9003, "Example3", 300, 150));
        entityManager.persist(new Item(9004, "Testing4", 400, 200));

        Page<Item> pagedEntity = repos.findAllByNameStartingWith("Test", PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Item(9001, "Test1", 100, 50),
                new Item(9004, "Testing4", 400, 200)
        );
    }

    @Test
    @DisplayName("Given JpaItemRepository, When invoke findById with id, Then should return matched content")
    void test_findById() {
        entityManager.persist(new Item(9001, "Test1", 100, 50));
        entityManager.persist(new Item(9002, "Test2", 200, 100));
        entityManager.persist(new Item(9003, "Test3", 300, 150));
        entityManager.persist(new Item(9004, "Test4", 400, 200));

        Optional<Item> optionalEntity = ((ItemRepository) repos).findById(9003);

        Assertions.assertThat(optionalEntity.isPresent()).isTrue();
        optionalEntity.ifPresent(entity -> {
            Assertions.assertThat(entity.getId()).isEqualTo(9003);
            Assertions.assertThat(entity.getName()).isEqualTo("Test3");
            Assertions.assertThat(entity.getPrice()).isEqualTo(300);
            Assertions.assertThat(entity.getSellPrice()).isEqualTo(150);
        });
    }
}
