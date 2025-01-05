package com.yo1000.api.presentation;

import com.yo1000.api.application.UserProfileApplicationService;
import com.yo1000.api.domain.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

@WebMvcTest(UserProfileRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ExceptionHandlerAdvice.class)
public class UserProfileRestControllerTests {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    UserProfileApplicationService service;

    @Test
    void test_get_withParams() {
        // Arrange
        Mockito.doReturn(new PageImpl<>(List.of(
                new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile2"))))
                .when(service)
                .search(Mockito.anyString(), Mockito.nullable(Pageable.class));

        // Act
        // Assert
        mockMvc.get().uri("/userProfiles")
                .param("username", "test2")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.content[0]", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("id", 9002)
                            .containsEntry("username", "test2")
                            .containsEntry("familyName", "Family2")
                            .containsEntry("givenName", "Given2")
                            .containsEntry("age", 22)
                            .containsEntry("gender", "Female")
                            .containsEntry("profile", "Profile2");
                });

        Mockito.verify(service).search(Mockito.eq("test2"), Mockito.any(Pageable.class));
    }

    @Test
    void test_get_withPathVar() {
        // Arrange
        Mockito.doReturn(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile1"))
                .when(service)
                .lookup(Mockito.anyInt());

        // Act
        // Assert
        mockMvc.get().uri("/userProfiles/9001")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$", assertProvider -> {
                    assertProvider
                            .assertThat()
                            .asMap()
                            .containsEntry("id", 9001)
                            .containsEntry("username", "test1")
                            .containsEntry("familyName", "Family1")
                            .containsEntry("givenName", "Given1")
                            .containsEntry("age", 21)
                            .containsEntry("gender", "Male")
                            .containsEntry("profile", "Profile1");
                });

        Mockito.verify(service).lookup(Mockito.eq(9001));
    }

    @Test
    void test_post() {
        // Arrange
        Mockito.doReturn(new UserProfile(9004, "test4", "Family4", "Given4", 24, "Female", "Profile4"))
                .when(service)
                .create(Mockito.any(UserProfile.class));

        // Act
        // Assert
        mockMvc.post().uri("/userProfiles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "id": 9004,
                            "username": "test4",
                            "familyName": "Family4",
                            "givenName": "Given4",
                            "age": 24,
                            "gender": "Female",
                            "profile": "Profile4"
                        }
                        """)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asMap()
                .containsEntry("id", 9004)
                .containsEntry("username", "test4")
                .containsEntry("familyName", "Family4")
                .containsEntry("givenName", "Given4")
                .containsEntry("age", 24)
                .containsEntry("gender", "Female")
                .containsEntry("profile", "Profile4");

        Mockito.verify(service).create(Mockito.eq(new UserProfile(
                9004,
                "test4",
                "Family4",
                "Given4",
                24,
                "Female",
                "Profile4"
        )));
    }

    @Test
    void test_patch_withParams() {
        // Arrange
        Mockito.doReturn(new UserProfile(9003, "test3", "Family5", "Given5", 25, "Male", "Profile5"))
                .when(service)
                .updateDiffByUsername(Mockito.anyString(), Mockito.anyString());

        // Act
        // Assert
        mockMvc.patch().uri("/userProfiles")
                .param("username", "test3")
                .contentType("application/merge-patch+json")
                .content("""
                        {
                            "id": 9003,
                            "username": "test3",
                            "familyName": "Family5",
                            "givenName": "Given5",
                            "age": 25,
                            "gender": "Male",
                            "profile": "Profile5"
                        }
                        """)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$")
                .asMap()
                .containsEntry("id", 9003)
                .containsEntry("username", "test3")
                .containsEntry("familyName", "Family5")
                .containsEntry("givenName", "Given5")
                .containsEntry("age", 25)
                .containsEntry("gender", "Male")
                .containsEntry("profile", "Profile5");

        Mockito.verify(service).updateDiffByUsername(Mockito.eq("test3"), Mockito.eq("""
                {
                    "id": 9003,
                    "username": "test3",
                    "familyName": "Family5",
                    "givenName": "Given5",
                    "age": 25,
                    "gender": "Male",
                    "profile": "Profile5"
                }
                """));
    }
}
