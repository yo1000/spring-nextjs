package com.yo1000.api.infrastructure;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.Weapon;
import com.yo1000.api.domain.model.WeaponMaterial;
import com.yo1000.api.domain.model.WeaponRemodel;
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

import java.util.List;

@DataJpaTest
@Testcontainers
public class JpaWeaponRemodelRepositoryTests {
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
    JpaWeaponRemodelRepository repos;

    @Test
    @DisplayName("Given JpaWeaponRemodelRepository, When invoke findAll with PageRequest, Then should return paged content")
    void test_findAll() {
        Weapon weapon1 = entityManager.persist(new Weapon(9001, "Test1", 110, 210));
        Weapon weapon2 = entityManager.persist(new Weapon(9002, "Test2", 120, 220));
        Weapon weapon3 = entityManager.persist(new Weapon(9003, "Test3", 130, 230));
        Weapon weapon4 = entityManager.persist(new Weapon(9004, "Test4", 140, 240));

        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Test2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Test3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Test4", 400, 200));

        WeaponMaterial material1 = entityManager.persist(new WeaponMaterial(90011, weapon1, item1, 1));
        WeaponMaterial material2 = entityManager.persist(new WeaponMaterial(90012, weapon1, item2, 2));
        WeaponMaterial material3 = entityManager.persist(new WeaponMaterial(90021, weapon2, item2, 3));
        WeaponMaterial material4 = entityManager.persist(new WeaponMaterial(90022, weapon2, item3, 4));
        WeaponMaterial material5 = entityManager.persist(new WeaponMaterial(90031, weapon3, item3, 5));
        WeaponMaterial material6 = entityManager.persist(new WeaponMaterial(90032, weapon3, item4, 6));
        WeaponMaterial material7 = entityManager.persist(new WeaponMaterial(90041, weapon4, item4, 7));
        WeaponMaterial material8 = entityManager.persist(new WeaponMaterial(90042, weapon4, item1, 8));

        entityManager.persist(new WeaponRemodel(9001, weapon1, 1000, List.of(material1, material2)));
        entityManager.persist(new WeaponRemodel(9002, weapon2, 2000, List.of(material3, material4)));
        entityManager.persist(new WeaponRemodel(9003, weapon3, 3000, List.of(material5, material6)));
        entityManager.persist(new WeaponRemodel(9004, weapon4, 4000, List.of(material7, material8)));

        Page<WeaponRemodel> pagedEntity = repos.findAll(PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).usingRecursiveFieldByFieldElementComparator().contains(
                new WeaponRemodel(9001, weapon1, 1000, List.of(
                        new WeaponMaterial(90011, weapon1, item1, 1),
                        new WeaponMaterial(90012, weapon1, item2, 2))),
                new WeaponRemodel(9002, weapon2, 2000, List.of(
                        new WeaponMaterial(90021, weapon2, item2, 3),
                        new WeaponMaterial(90022, weapon2, item3, 4))),
                new WeaponRemodel(9003, weapon3, 3000, List.of(
                        new WeaponMaterial(90031, weapon3, item3, 5),
                        new WeaponMaterial(90032, weapon3, item4, 6)))
        );
    }

    @Test
    @DisplayName("Given JpaWeaponRemodelRepository, When invoke findAllByWeaponNameStartingWith with weaponName, Then should return left-hand match filtered content")
    void test_findAllByWeaponNameStartingWith() {
        Weapon weapon1 = entityManager.persist(new Weapon(9001, "Test1", 110, 210));
        Weapon weapon2 = entityManager.persist(new Weapon(9002, "Demo2", 120, 220));
        Weapon weapon3 = entityManager.persist(new Weapon(9003, "Example3", 130, 230));
        Weapon weapon4 = entityManager.persist(new Weapon(9004, "Testing4", 140, 240));

        Item item1 = entityManager.persist(new Item(9001, "Test1", 100, 50));
        Item item2 = entityManager.persist(new Item(9002, "Test2", 200, 100));
        Item item3 = entityManager.persist(new Item(9003, "Test3", 300, 150));
        Item item4 = entityManager.persist(new Item(9004, "Test4", 400, 200));

        WeaponMaterial material1 = entityManager.persist(new WeaponMaterial(90011, weapon1, item1, 1));
        WeaponMaterial material2 = entityManager.persist(new WeaponMaterial(90012, weapon1, item2, 2));
        WeaponMaterial material3 = entityManager.persist(new WeaponMaterial(90021, weapon2, item2, 3));
        WeaponMaterial material4 = entityManager.persist(new WeaponMaterial(90022, weapon2, item3, 4));
        WeaponMaterial material5 = entityManager.persist(new WeaponMaterial(90031, weapon3, item3, 5));
        WeaponMaterial material6 = entityManager.persist(new WeaponMaterial(90032, weapon3, item4, 6));
        WeaponMaterial material7 = entityManager.persist(new WeaponMaterial(90041, weapon4, item4, 7));
        WeaponMaterial material8 = entityManager.persist(new WeaponMaterial(90042, weapon4, item1, 8));

        entityManager.persist(new WeaponRemodel(9001, weapon1, 1000, List.of(material1, material2)));
        entityManager.persist(new WeaponRemodel(9002, weapon2, 2000, List.of(material3, material4)));
        entityManager.persist(new WeaponRemodel(9003, weapon3, 3000, List.of(material5, material6)));
        entityManager.persist(new WeaponRemodel(9004, weapon4, 4000, List.of(material7, material8)));

        Page<WeaponRemodel> pagedEntity = repos.findAllByWeaponNameStartingWith("Test", PageRequest.of(0, 3));

        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).usingRecursiveFieldByFieldElementComparator().contains(
                new WeaponRemodel(9001, weapon1, 1000, List.of(
                        new WeaponMaterial(90011, weapon1, item1, 1),
                        new WeaponMaterial(90012, weapon1, item2, 2))),
                new WeaponRemodel(9004, weapon4, 4000, List.of(
                        new WeaponMaterial(90041, weapon4, item4, 7),
                        new WeaponMaterial(90042, weapon4, item1, 8)))
        );
    }
}
