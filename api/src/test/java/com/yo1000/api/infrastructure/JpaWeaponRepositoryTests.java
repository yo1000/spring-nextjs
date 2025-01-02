package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.Weapon;
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

@DataJpaTest
@Testcontainers
public class JpaWeaponRepositoryTests {
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
    JpaWeaponRepository repos;

    @Test
    @DisplayName("Given JpaWeaponRepository, When invoke findAll with PageRequest, Then should return paged content")
    void test_findAll() {
        entityManager.persist(new Weapon(9001, "Test1", 110, 210));
        entityManager.persist(new Weapon(9002, "Test2", 120, 220));
        entityManager.persist(new Weapon(9003, "Test3", 130, 230));
        entityManager.persist(new Weapon(9004, "Test4", 140, 240));

        Page<Weapon> pagedEntity = repos.findAll(PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Weapon(9001, "Test1", 110, 210),
                new Weapon(9002, "Test2", 120, 220),
                new Weapon(9003, "Test3", 130, 230)
        );
    }

    @Test
    @DisplayName("Given JpaWeaponRepository, When invoke findAllByNameStartingWith with name, Then should return left-hand match filtered content")
    void test_findAllByNameStartingWith() {
        entityManager.persist(new Weapon(9001, "Test1", 110, 210));
        entityManager.persist(new Weapon(9002, "Demo2", 120, 220));
        entityManager.persist(new Weapon(9003, "Example3", 130, 230));
        entityManager.persist(new Weapon(9004, "Testing4", 140, 240));

        Page<Weapon> pagedEntity = repos.findAllByNameStartingWith("Test", PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Weapon(9001, "Test1", 110, 210),
                new Weapon(9004, "Testing4", 140, 240)
        );
    }
}
