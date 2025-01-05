package com.yo1000.api.presentation;

import com.yo1000.api.application.WeaponRemodelApplicationService;
import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.Weapon;
import com.yo1000.api.domain.model.WeaponMaterial;
import com.yo1000.api.domain.model.WeaponRemodel;
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
import java.util.Map;

@WebMvcTest(WeaponRemodelRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class WeaponRemodelRestControllerTests {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    WeaponRemodelApplicationService service;

    @Test
    void test_get() {
        // Arrange
        Mockito.doReturn(
                new PageImpl<>(List.of(
                        new WeaponRemodel(
                                9001,
                                new Weapon(9001, "Test1", 110, 210),
                                1100,
                                List.of(new WeaponMaterial(
                                        9001,
                                        new Weapon(9001, "Test1", 110, 210),
                                        new Item(9001, "Test1", 1000, 500),
                                        1))),
                        new WeaponRemodel(
                                9002,
                                new Weapon(9002, "Test2", 120, 220),
                                1200,
                                List.of(new WeaponMaterial(
                                        9002,
                                        new Weapon(9002, "Test2", 120, 220),
                                        new Item(9002, "Test2", 2000, 1000),
                                        2))),
                        new WeaponRemodel(
                                9003,
                                new Weapon(9003, "Test3", 130, 230),
                                1300,
                                List.of(new WeaponMaterial(
                                        9003,
                                        new Weapon(9003, "Test3", 130, 230),
                                        new Item(9003, "Test3", 3000, 1500),
                                        3))))))
                .when(service)
                .list(Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/weaponRemodels")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9001)
                        .containsEntry("weapon", Map.ofEntries(
                                Map.entry("id", 9001),
                                Map.entry("name", "Test1"),
                                Map.entry("str", 110),
                                Map.entry("hit", 210)
                        ))
                        .containsEntry("price", 1100)
                        .containsEntry("materials", List.of(Map.ofEntries(
                                Map.entry("id", 9001),
                                Map.entry("weapon", Map.ofEntries(
                                        Map.entry("id", 9001),
                                        Map.entry("name", "Test1"),
                                        Map.entry("str", 110),
                                        Map.entry("hit", 210)
                                )),
                                Map.entry("item", Map.ofEntries(
                                        Map.entry("id", 9001),
                                        Map.entry("name", "Test1"),
                                        Map.entry("price", 1000),
                                        Map.entry("sellPrice", 500)
                                )),
                                Map.entry("quantity", 1)
                        ))))
                .hasPathSatisfying("$.content[1]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9002)
                        .containsEntry("weapon", Map.ofEntries(
                                Map.entry("id", 9002),
                                Map.entry("name", "Test2"),
                                Map.entry("str", 120),
                                Map.entry("hit", 220)
                        ))
                        .containsEntry("price", 1200)
                        .containsEntry("materials", List.of(Map.ofEntries(
                                Map.entry("id", 9002),
                                Map.entry("weapon", Map.ofEntries(
                                        Map.entry("id", 9002),
                                        Map.entry("name", "Test2"),
                                        Map.entry("str", 120),
                                        Map.entry("hit", 220)
                                )),
                                Map.entry("item", Map.ofEntries(
                                        Map.entry("id", 9002),
                                        Map.entry("name", "Test2"),
                                        Map.entry("price", 2000),
                                        Map.entry("sellPrice", 1000)
                                )),
                                Map.entry("quantity", 2)
                        ))))
                .hasPathSatisfying("$.content[2]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9003)
                        .containsEntry("weapon", Map.ofEntries(
                                Map.entry("id", 9003),
                                Map.entry("name", "Test3"),
                                Map.entry("str", 130),
                                Map.entry("hit", 230)
                        ))
                        .containsEntry("price", 1300)
                        .containsEntry("materials", List.of(Map.ofEntries(
                                Map.entry("id", 9003),
                                Map.entry("weapon", Map.ofEntries(
                                        Map.entry("id", 9003),
                                        Map.entry("name", "Test3"),
                                        Map.entry("str", 130),
                                        Map.entry("hit", 230)
                                )),
                                Map.entry("item", Map.ofEntries(
                                        Map.entry("id", 9003),
                                        Map.entry("name", "Test3"),
                                        Map.entry("price", 3000),
                                        Map.entry("sellPrice", 1500)
                                )),
                                Map.entry("quantity", 3)
                        ))));

        Mockito.verify(service).list(Mockito.any(Pageable.class));
    }

    @Test
    void test_get_withParams() {
        // Arrange
        Mockito.doReturn(new PageImpl<>(List.of(
                        new WeaponRemodel(
                                9001,
                                new Weapon(9001, "Test1", 110, 210),
                                1100,
                                List.of(new WeaponMaterial(
                                        9001,
                                        new Weapon(9001, "Test1", 110, 210),
                                        new Item(9001, "Test1", 1000, 500),
                                        1))),
                        new WeaponRemodel(
                                9004,
                                new Weapon(9004, "Testing4", 140, 240),
                                1400,
                                List.of(new WeaponMaterial(
                                        9004,
                                        new Weapon(9004, "Testing4", 140, 240),
                                        new Item(9004, "Testing4", 4000, 2000),
                                        4))))))
                .when(service)
                .search(Mockito.anyString(), Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/weaponRemodels")
                .param("weaponName", "Test")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9001)
                        .containsEntry("weapon", Map.ofEntries(
                                Map.entry("id", 9001),
                                Map.entry("name", "Test1"),
                                Map.entry("str", 110),
                                Map.entry("hit", 210)
                        ))
                        .containsEntry("price", 1100)
                        .containsEntry("materials", List.of(Map.ofEntries(
                                Map.entry("id", 9001),
                                Map.entry("weapon", Map.ofEntries(
                                        Map.entry("id", 9001),
                                        Map.entry("name", "Test1"),
                                        Map.entry("str", 110),
                                        Map.entry("hit", 210)
                                )),
                                Map.entry("item", Map.ofEntries(
                                        Map.entry("id", 9001),
                                        Map.entry("name", "Test1"),
                                        Map.entry("price", 1000),
                                        Map.entry("sellPrice", 500)
                                )),
                                Map.entry("quantity", 1)
                        ))))
                .hasPathSatisfying("$.content[1]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9004)
                        .containsEntry("weapon", Map.ofEntries(
                                Map.entry("id", 9004),
                                Map.entry("name", "Testing4"),
                                Map.entry("str", 140),
                                Map.entry("hit", 240)
                        ))
                        .containsEntry("price", 1400)
                        .containsEntry("materials", List.of(Map.ofEntries(
                                Map.entry("id", 9004),
                                Map.entry("weapon", Map.ofEntries(
                                        Map.entry("id", 9004),
                                        Map.entry("name", "Testing4"),
                                        Map.entry("str", 140),
                                        Map.entry("hit", 240)
                                )),
                                Map.entry("item", Map.ofEntries(
                                        Map.entry("id", 9004),
                                        Map.entry("name", "Testing4"),
                                        Map.entry("price", 4000),
                                        Map.entry("sellPrice", 2000)
                                )),
                                Map.entry("quantity", 4)
                        ))));

        Mockito.verify(service).search(Mockito.eq("Test"), Mockito.any(Pageable.class));
    }
}
