package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.ItemInventory;
import com.yo1000.api.domain.repository.ItemInventoryRepository;
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
public class JpaItemInventoryRepositoryTests {
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
    JpaItemInventoryRepository repos;

    @Test
    @DisplayName("Given JpaItemInventoryRepository, When invoke findAll with PageRequest, Then should return paged content")
    void test_findAll() {
        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Test2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Test3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Test4", 400, 200));

        entityManager.persist(new ItemInventory(9001, item1, 10));
        entityManager.persist(new ItemInventory(9002, item2, 20));
        entityManager.persist(new ItemInventory(9003, item3, 30));
        entityManager.persist(new ItemInventory(9004, item4, 40));

        Page<ItemInventory> pagedEntity = repos.findAll(PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new ItemInventory(9001, item1, 10),
                new ItemInventory(9002, item2, 20),
                new ItemInventory(9003, item3, 30)
        );
    }

    @Test
    @DisplayName("Given JpaItemInventoryRepository, When invoke findAllByItemNameStartingWith with itemName, Then should return left-hand match filtered content")
    void test_findAllByItemNameStartingWith() {
        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Demo2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Example3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Testing4", 400, 200));

        entityManager.persist(new ItemInventory(9001, item1, 10));
        entityManager.persist(new ItemInventory(9002, item2, 20));
        entityManager.persist(new ItemInventory(9003, item3, 30));
        entityManager.persist(new ItemInventory(9004, item4, 40));

        Page<ItemInventory> pagedEntity = repos.findAllByItemNameStartingWith("Test", PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new ItemInventory(9001, item1, 10),
                new ItemInventory(9004, item4, 40)
        );
    }

    @Test
    @DisplayName("Given JpaItemInventoryRepository, When invoke findById with id, Then should return matched content")
    void test_findById() {
        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Test2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Test3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Test4", 400, 200));

        entityManager.persist(new ItemInventory(9001, item1, 10));
        entityManager.persist(new ItemInventory(9002, item2, 20));
        entityManager.persist(new ItemInventory(9003, item3, 30));
        entityManager.persist(new ItemInventory(9004, item4, 40));

        Optional<ItemInventory> optionalEntity = ((ItemInventoryRepository) repos).findById(9003);

        Assertions.assertThat(optionalEntity.isPresent()).isTrue();
        optionalEntity.ifPresent(entity -> {
            Assertions.assertThat(entity.getId()).isEqualTo(9003);
            Assertions.assertThat(entity.getItem()).isEqualTo(item3);
            Assertions.assertThat(entity.getQuantity()).isEqualTo(30);
        });
    }

    @Test
    @DisplayName("Given JpaItemInventoryRepository, When invoke findByItemId with itemId, Then should return matched content")
    void test_findByItemId() {
        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Test2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Test3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Test4", 400, 200));

        entityManager.persist(new ItemInventory(9001, item1, 10));
        entityManager.persist(new ItemInventory(9002, item2, 20));
        entityManager.persist(new ItemInventory(9003, item3, 30));
        entityManager.persist(new ItemInventory(9004, item4, 40));

        Optional<ItemInventory> optionalEntity = repos.findByItemId(9002);

        Assertions.assertThat(optionalEntity.isPresent()).isTrue();
        optionalEntity.ifPresent(entity -> {
            Assertions.assertThat(entity.getId()).isEqualTo(9002);
            Assertions.assertThat(entity.getItem()).isEqualTo(item2);
            Assertions.assertThat(entity.getQuantity()).isEqualTo(20);
        });
    }

    @Test
    @DisplayName("Given JpaItemInventoryRepository, When invoke save, Then should store content")
    void test_save() {
        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Test2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Test3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Test4", 400, 200));
        Item item5 = entityManager.persist(new Item(9005, "Test5", 500, 250));

        entityManager.persist(new ItemInventory(9001, item1, 10));
        entityManager.persist(new ItemInventory(9002, item2, 20));
        entityManager.persist(new ItemInventory(9003, item3, 30));
        entityManager.persist(new ItemInventory(9004, item4, 40));

        ItemInventory savedEntity = ((ItemInventoryRepository) repos).save(new ItemInventory(9005, item5, 50));

        Assertions.assertThat(savedEntity.getId()).isEqualTo(9005);
        Assertions.assertThat(savedEntity.getItem()).isEqualTo(item5);
        Assertions.assertThat(savedEntity.getQuantity()).isEqualTo(50);
    }
}
