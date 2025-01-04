package com.yo1000.api.presentation;

import com.yo1000.api.application.ItemApplicationService;
import com.yo1000.api.domain.model.Item;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

@WebMvcTest(ItemRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemRestControllerTests {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    ItemApplicationService service;

    @Test
    void test_get() {
        // Arrange
        Mockito.doReturn(
                new PageImpl<>(List.of(
                        new Item(9001, "Test1", 1000, 500),
                        new Item(9002, "Test2", 2000, 1000))))
                .when(service)
                .list(Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/items")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9001)
                        .containsEntry("name", "Test1")
                        .containsEntry("price", 1000)
                        .containsEntry("sellPrice", 500))
                .hasPathSatisfying("$.content[1]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9002)
                        .containsEntry("name", "Test2")
                        .containsEntry("price", 2000)
                        .containsEntry("sellPrice", 1000));

        Mockito.verify(service).list(Mockito.any(Pageable.class));
    }

    @Test
    void test_get_withParams() {
        // Arrange
        Mockito.doReturn(new PageImpl<>(List.of(new Item(9001, "Test1", 1000, 500))))
                .when(service)
                .search(Mockito.anyString(), Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/items")
                .param("name", "Test1")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.content[0]")
                .asMap()
                .containsEntry("id", 9001)
                .containsEntry("name", "Test1")
                .containsEntry("price", 1000)
                .containsEntry("sellPrice", 500);

        Mockito.verify(service).search(Mockito.eq("Test1"), Mockito.any(Pageable.class));
    }

    @Test
    void test_get_withPathVar() {
        // Arrange
        Mockito.doReturn(new Item(9001, "Test1", 1000, 500))
                .when(service)
                .lookup(Mockito.anyInt());

        // Act
        // Assert
        mockMvc.get().uri("/items/9001")
                .param("name", "Test1")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asMap()
                .containsEntry("id", 9001)
                .containsEntry("name", "Test1")
                .containsEntry("price", 1000)
                .containsEntry("sellPrice", 500);

        Mockito.verify(service).lookup(Mockito.eq(9001));
    }
}
