package com.yo1000.api.application;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ItemApplicationServiceTests {
    @Test
    void test_list() {
        // Arrange
        ItemRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new Item(9001, "Test1", 100, 50),
                                new Item(9002, "Test2", 200, 100)),
                        PageRequest.of(0, 3),
                        2))
                .when(mockedRepos)
                .findAll(Mockito.any());

        ItemApplicationService service = new ItemApplicationService(mockedRepos);

        // Act
        Page<Item> pagedEntity = service.list(PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Item(9001, "Test1", 100, 50),
                new Item(9002, "Test2", 200, 100));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAll(PageRequest.of(0, 3));
    }

    @Test
    void test_search() {
        // Arrange
        ItemRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new Item(9002, "Test2", 200, 100)),
                        PageRequest.of(0, 3),
                        1))
                .when(mockedRepos)
                .findAllByNameStartingWith(Mockito.any(), Mockito.any(Pageable.class));

        ItemApplicationService service = new ItemApplicationService(mockedRepos);

        // Act
        Page<Item> pagedEntity = service.search("Test2", PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(1);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(1);
        Assertions.assertThat(pagedEntity.getContent()).contains(
                new Item(9002, "Test2", 200, 100));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAllByNameStartingWith("Test2", PageRequest.of(0, 3));
    }

    @Test
    void test_lookup() {
        // Arrange
        ItemRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(new Item(9002, "Test2", 200, 100)))
                .when(mockedRepos)
                .findById(Mockito.any());

        ItemApplicationService service = new ItemApplicationService(mockedRepos);

        // Act
        Item entity = service.lookup(9002);

        // Assert
        Assertions.assertThat(entity).isEqualTo(new Item(9002, "Test2", 200, 100));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(9002);
    }
}
