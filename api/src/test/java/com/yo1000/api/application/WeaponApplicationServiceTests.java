package com.yo1000.api.application;

import com.yo1000.api.domain.model.Weapon;
import com.yo1000.api.domain.repository.WeaponRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class WeaponApplicationServiceTests {
    @Test
    void test_list() {
        // Arrange
        WeaponRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new Weapon(9001, "Test1", 110, 210),
                                new Weapon(9002, "Test2", 120, 220),
                                new Weapon(9003, "Test3", 130, 230)),
                        PageRequest.of(0, 3),
                        3))
                .when(mockedRepos)
                .findAll(Mockito.any());

        WeaponApplicationService service = new WeaponApplicationService(mockedRepos);

        // Act
        Page<Weapon> pagedEntity = service.list(PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(3);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Weapon(9001, "Test1", 110, 210),
                new Weapon(9002, "Test2", 120, 220),
                new Weapon(9003, "Test3", 130, 230));

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
