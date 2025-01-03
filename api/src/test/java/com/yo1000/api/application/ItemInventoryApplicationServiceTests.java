package com.yo1000.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.ItemInventory;
import com.yo1000.api.domain.repository.ItemInventoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ItemInventoryApplicationServiceTests {
    @Test
    void test_list() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new ItemInventory(9001, new Item(9001, "Test1", 100, 50), 10),
                                new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20)),
                        PageRequest.of(0, 3),
                        2))
                .when(mockedRepos)
                .findAll(Mockito.any());

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        Page<ItemInventory> pagedEntity = service.list(PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(2);
        Assertions.assertThat(pagedEntity.getContent()).usingRecursiveFieldByFieldElementComparator().contains(
                new ItemInventory(9001, new Item(9001, "Test1", 100, 50), 10),
                new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAll(PageRequest.of(0, 3));
    }

    @Test
    void test_search() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(new PageImpl<>(
                        List.of(
                                new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20)),
                        PageRequest.of(0, 3),
                        1))
                .when(mockedRepos)
                .findAllByItemNameStartingWith(Mockito.any(), Mockito.any(Pageable.class));

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        Page<ItemInventory> pagedEntity = service.search("Test2", PageRequest.of(0, 3));

        // Assert
        Assertions.assertThat(pagedEntity).hasSize(1);
        Assertions.assertThat(pagedEntity.getContent()).hasSize(1);
        Assertions.assertThat(pagedEntity.getContent()).usingRecursiveFieldByFieldElementComparator().contains(
                new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findAllByItemNameStartingWith("Test2", PageRequest.of(0, 3));
    }

    @Test
    void test_lookup() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20)))
                .when(mockedRepos)
                .findById(Mockito.any());

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.lookup(9002);

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(9002);
    }

    @Test
    void test_lookupByItemId() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20)))
                .when(mockedRepos)
                .findByItemId(Mockito.any());

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.lookupByItemId(9002);

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9002, new Item(9002, "Test2", 200, 100), 20));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findByItemId(9002);
    }

    @Test
    void test_create() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.empty())
                .when(mockedRepos)
                .findById(Mockito.any());
        Mockito.doReturn(new ItemInventory(9003, new Item(9003, "Test3", 300, 150), 30))
                .when(mockedRepos)
                .save(Mockito.any(ItemInventory.class));

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.create(new ItemInventory(null, new Item(9003, "Test3", 300, 150), 30));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9003, new Item(9003, "Test3", 300, 150), 30));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(null);
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new ItemInventory(null, new Item(9003, "Test3", 300, 150), 30));
    }

    @Test
    void test_update() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                new ItemInventory(9003, new Item(9003, "Test3", 300, 150), 30)))
                .when(mockedRepos)
                .findById(Mockito.any());
        Mockito.doReturn(
                new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40))
                .when(mockedRepos)
                .save(Mockito.any(ItemInventory.class));

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.update(9003, new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(9003);
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));
    }

    @Test
    void test_updateByItemId() {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new ItemInventory(9003, new Item(9003, "Test3", 300, 150), 30)))
                .when(mockedRepos)
                .findByItemId(Mockito.any());
        Mockito.doReturn(
                        new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40))
                .when(mockedRepos)
                .save(Mockito.any(ItemInventory.class));

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.updateByItemId(9003, new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findByItemId(9003);
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));
    }

    @Test
    void test_updateDiff() throws Throwable {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new ItemInventory(9003, new Item(9003, "Test3", 300, 150), 30)))
                .when(mockedRepos)
                .findById(Mockito.any());
        Mockito.doReturn(
                        new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40))
                .when(mockedRepos)
                .save(Mockito.any(ItemInventory.class));

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.updateDiff(9003, objectMapper.writeValueAsString(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40)));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findById(9003);
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));
    }

    @Test
    void test_updateDiffByItemId() throws Throwable {
        // Arrange
        ItemInventoryRepository mockedRepos = Mockito.mock();
        Mockito.doReturn(Optional.of(
                        new ItemInventory(9003, new Item(9003, "Test3", 300, 150), 30)))
                .when(mockedRepos)
                .findByItemId(Mockito.any());
        Mockito.doReturn(
                        new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40))
                .when(mockedRepos)
                .save(Mockito.any(ItemInventory.class));

        ObjectMapper objectMapper = new ObjectMapper();

        ItemInventoryApplicationService service = new ItemInventoryApplicationService(mockedRepos, objectMapper);

        // Act
        ItemInventory entity = service.updateDiffByItemId(9003, objectMapper.writeValueAsString(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40)));

        // Assert
        Assertions.assertThat(entity).isEqualTo(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));

        Mockito.verify(mockedRepos, Mockito.times(1))
                .findByItemId(9003);
        Mockito.verify(mockedRepos, Mockito.times(1))
                .save(new ItemInventory(9003, new Item(9004, "Test4", 400, 200), 40));
    }
}
