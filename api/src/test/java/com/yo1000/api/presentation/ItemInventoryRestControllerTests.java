package com.yo1000.api.presentation;

import com.yo1000.api.application.ItemInventoryApplicationService;
import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.ItemInventory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;
import java.util.Map;

@WebMvcTest(ItemInventoryRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ExceptionHandlerAdvice.class)
public class ItemInventoryRestControllerTests {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    ItemInventoryApplicationService service;

    @Test
    void test_get_withParams() {
        // Arrange
        Mockito.doReturn(new PageImpl<>(List.of(
                new ItemInventory(9001, new Item(9001, "Test1", 1000, 500), 10))))
                .when(service)
                .search(Mockito.anyString(), Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/itemInventories")
                .param("itemName", "Test1")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("id", 9001)
                            .containsEntry("item", Map.ofEntries(
                                    Map.entry("id", 9001),
                                    Map.entry("name", "Test1"),
                                    Map.entry("price", 1000),
                                    Map.entry("sellPrice", 500)))
                            .containsEntry("quantity", 10);
                });

        Mockito.verify(service).search(Mockito.eq("Test1"), Mockito.any(Pageable.class));
    }

    @Test
    void test_post() {
        // Arrange
        Mockito.doReturn(new ItemInventory(9001, new Item(9001, "Test1", 1000, 500), 10))
                .when(service)
                .create(Mockito.any(ItemInventory.class));

        // Act
        // Assert
        mockMvc.post().uri("/itemInventories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "id": 9001,
                            "item": {
                                "id": 9001,
                                "name": "Test1",
                                "price": 1000,
                                "sellPrice": 500
                            },
                            "quantity": 10
                        }
                        """)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asMap()
                .containsEntry("id", 9001)
                .containsEntry("item", Map.ofEntries(
                        Map.entry("id", 9001),
                        Map.entry("name", "Test1"),
                        Map.entry("price", 1000),
                        Map.entry("sellPrice", 500)))
                .containsEntry("quantity", 10);

        Mockito.verify(service).create(Mockito.eq(new ItemInventory(
                9001,
                new Item(
                        9001,
                        "Test1",
                        1000,
                        500
                ),
                10
        )));
    }

    @Test
    void test_post_invalid() {
        // Arrange
        Mockito.doReturn(new ItemInventory(9001, new Item(9001, "Test1", 1000, 500), 10))
                .when(service)
                .create(Mockito.any(ItemInventory.class));

        // Act
        // Assert
        mockMvc.post().uri("/itemInventories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "id": 9001,
                            "item": {
                                "id": 9001,
                                "name": "Test1",
                                "price": 1000,
                                "sellPrice": 500
                            },
                            "quantity": -1
                        }
                        """)
                .assertThat()
                .debug()
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .extractingPath("$.parameters.quantity")
                .asMap()
                .containsEntry("value", -1);

        Mockito.verifyNoInteractions(service);
    }

    @Test
    void test_patch_withPathVar() {
        // Arrange
        Mockito.doReturn(new ItemInventory(9001, new Item(9001, "Test1", 1000, 500), 10))
                .when(service)
                .updateDiff(Mockito.anyInt(), Mockito.anyString());

        // Act
        // Assert
        mockMvc.patch().uri("/itemInventories/9001")
                .contentType("application/merge-patch+json")
                .content("""
                        {
                            "id": 9001,
                            "item": {
                                "id": 9001,
                                "name": "Test1",
                                "price": 1000,
                                "sellPrice": 500
                            },
                            "quantity": 10
                        }
                        """)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asMap()
                .containsEntry("id", 9001)
                .containsEntry("item", Map.ofEntries(
                        Map.entry("id", 9001),
                        Map.entry("name", "Test1"),
                        Map.entry("price", 1000),
                        Map.entry("sellPrice", 500)))
                .containsEntry("quantity", 10);

        Mockito.verify(service).updateDiff(Mockito.eq(9001), Mockito.eq("""
                {
                    "id": 9001,
                    "item": {
                        "id": 9001,
                        "name": "Test1",
                        "price": 1000,
                        "sellPrice": 500
                    },
                    "quantity": 10
                }
                """));
    }

    @Test
    void test_patch_withParams() {
        // Arrange
        Mockito.doReturn(new ItemInventory(9001, new Item(9001, "Test1", 1000, 500), 10))
                .when(service)
                .updateDiffByItemId(Mockito.anyInt(), Mockito.anyString());

        // Act
        // Assert
        mockMvc.patch().uri("/itemInventories")
                .param("itemId", "9001")
                .contentType("application/merge-patch+json")
                .content("""
                        {
                            "id": 9001,
                            "item": {
                                "id": 9001,
                                "name": "Test1",
                                "price": 1000,
                                "sellPrice": 500
                            },
                            "quantity": 10
                        }
                        """)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asMap()
                .containsEntry("id", 9001)
                .containsEntry("item", Map.ofEntries(
                        Map.entry("id", 9001),
                        Map.entry("name", "Test1"),
                        Map.entry("price", 1000),
                        Map.entry("sellPrice", 500)))
                .containsEntry("quantity", 10);

        Mockito.verify(service).updateDiffByItemId(Mockito.eq(9001), Mockito.eq("""
                {
                    "id": 9001,
                    "item": {
                        "id": 9001,
                        "name": "Test1",
                        "price": 1000,
                        "sellPrice": 500
                    },
                    "quantity": 10
                }
                """));
    }
}
