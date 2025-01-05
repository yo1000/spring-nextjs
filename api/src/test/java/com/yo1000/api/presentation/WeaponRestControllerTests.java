package com.yo1000.api.presentation;

import com.yo1000.api.application.WeaponApplicationService;
import com.yo1000.api.domain.model.Weapon;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

@WebMvcTest(WeaponRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ExceptionHandlerAdvice.class)
public class WeaponRestControllerTests {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    WeaponApplicationService service;

    @Test
    void test_get() {
        // Arrange
        Mockito.doReturn(
                new PageImpl<>(List.of(
                        new Weapon(9001, "Test1", 110, 210),
                        new Weapon(9002, "Test2", 120, 220),
                        new Weapon(9003, "Test3", 130, 230))))
                .when(service)
                .list(Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/weapons")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9001)
                        .containsEntry("name", "Test1")
                        .containsEntry("str", 110)
                        .containsEntry("hit", 210))
                .hasPathSatisfying("$.content[1]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9002)
                        .containsEntry("name", "Test2")
                        .containsEntry("str", 120)
                        .containsEntry("hit", 220))
                .hasPathSatisfying("$.content[2]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9003)
                        .containsEntry("name", "Test3")
                        .containsEntry("str", 130)
                        .containsEntry("hit", 230));

        Mockito.verify(service).list(Mockito.any(Pageable.class));
    }

    @Test
    void test_get_withParams() {
        // Arrange
        Mockito.doReturn(new PageImpl<>(List.of(
                        new Weapon(9001, "Test1", 110, 210),
                        new Weapon(9004, "Testing4", 140, 240))))
                .when(service)
                .search(Mockito.anyString(), Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/weapons")
                .param("name", "Test")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9001)
                        .containsEntry("name", "Test1")
                        .containsEntry("str", 110)
                        .containsEntry("hit", 210))
                .hasPathSatisfying("$.content[1]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9004)
                        .containsEntry("name", "Testing4")
                        .containsEntry("str", 140)
                        .containsEntry("hit", 240));

        Mockito.verify(service).search(Mockito.eq("Test"), Mockito.any(Pageable.class));
    }
}
