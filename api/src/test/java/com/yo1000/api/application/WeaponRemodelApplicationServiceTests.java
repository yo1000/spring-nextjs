package com.yo1000.api.application;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.Weapon;
import com.yo1000.api.domain.model.WeaponMaterial;
import com.yo1000.api.domain.model.WeaponRemodel;
import com.yo1000.api.domain.repository.WeaponRemodelRepository;
import com.yo1000.api.domain.repository.WeaponRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class WeaponRemodelApplicationServiceTests {
    @Test
    void test_list() {
        // Arrange
        WeaponRemodelRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(List.of(
                        new WeaponRemodel(
                                9001,
                                new Weapon(9001, "Test1", 110, 210),
                                1100,
                                List.of(new WeaponMaterial(
                                        9001,
                                        new Weapon(9001, "Test1", 110, 210),
                                        new Item(9001, "Test1", 100, 50),
                                        10
                                ))),
                        new WeaponRemodel(
                                9002,
                                new Weapon(9002, "Test2", 120, 220),
                                1200,
                                List.of(new WeaponMaterial(
                                        9002,
                                        new Weapon(9002, "Test2", 120, 220),
                                        new Item(9002, "Test2", 200, 100),
                                        20
                                )))),
                        PageRequest.of(0, 2),
                        2))
                .when(mockedRepos)
                .findAll(Mockito.any());

        WeaponRemodelApplicationService service = new WeaponRemodelApplicationService(mockedRepos);

        // Act
        Page<WeaponRemodel> pagedEntity = service.list(PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new WeaponRemodel(
                        9001,
                        new Weapon(9001, "Test1", 110, 210),
                        1100,
                        List.of(new WeaponMaterial(
                                9001,
                                new Weapon(9001, "Test1", 110, 210),
                                new Item(9001, "Test1", 100, 50),
                                10
                        ))),
                new WeaponRemodel(
                        9002,
                        new Weapon(9002, "Test2", 120, 220),
                        1200,
                        List.of(new WeaponMaterial(
                                9002,
                                new Weapon(9002, "Test2", 120, 220),
                                new Item(9002, "Test2", 200, 100),
                                20
                        ))));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAll(PageRequest.of(0, 3));
    }

    @Test
    void test_search() {
        // Arrange
        WeaponRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new Weapon(9002, "Test2", 120, 220)),
                        PageRequest.of(0, 3),
                        1))
                .when(mockedRepos)
                .findAllByNameStartingWith(Mockito.any(), Mockito.any(Pageable.class));

        WeaponApplicationService service = new WeaponApplicationService(mockedRepos);

        // Act
        Page<Weapon> pagedEntity = service.search("Test2", PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(1);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(1);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Weapon(9002, "Test2", 120, 220));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAllByNameStartingWith("Test2", PageRequest.of(0, 3));
    }
}
