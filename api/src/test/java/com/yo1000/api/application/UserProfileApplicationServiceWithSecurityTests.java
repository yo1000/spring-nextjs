package com.yo1000.api.application;

import com.yo1000.api.config.SecurityConfig;
import com.yo1000.api.domain.model.UserProfile;
import com.yo1000.api.domain.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceInitializationAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@SpringBootTest(
        classes = {
                UserProfileApplicationService.class,
                SecurityConfig.class,
                DataSourceInitializationAutoConfiguration.class,
                JacksonAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
        },
        properties = {
                "app.security.idp=test",
                "app.security.cors.allowed-origins=http://test",
        })
public class UserProfileApplicationServiceWithSecurityTests {
    @MockitoSpyBean
    UserProfileApplicationService service;

    @MockitoBean
    UserProfileRepository repos;

    @Autowired
    ObjectMapper objectMapper;

    /* list */

    @Test
    void test_list_withUnauthorized() {
        // Arrange
        Mockito.doReturn(Mockito.mock(Page.class)).when(repos).findAll(Mockito.any(Pageable.class));

        // Act
        try {
            service.list(Mockito.mock(PageRequest.of(0, 20)));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithAnonymousUser
    void test_list_withAnonymousUser() {
        // Arrange
        Mockito.doReturn(Mockito.mock(Page.class)).when(repos).findAll(Mockito.any(Pageable.class));

        // Act
        try {
            service.list(PageRequest.of(0, 20));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"spring-nextjs.userProfile:read"})
    void test_list_withAnyPermission() {
        // Arrange
        Mockito.doReturn(Mockito.mock(Page.class)).when(repos).findAll(Mockito.any(Pageable.class));

        // Act
        try {
            service.list(PageRequest.of(0, 20));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /* search */

    @Test
    void test_search_withUnauthorized() {
        // Arrange
        Mockito.doReturn(Mockito.mock(Page.class)).when(repos)
                .findAllByUsernameStartingWith(Mockito.anyString(), Mockito.any(Pageable.class));

        // Act
        try {
            service.search("testuser", Mockito.mock(PageRequest.of(0, 20)));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithAnonymousUser
    void test_search_withAnonymousUser() {
        // Arrange
        Mockito.doReturn(Mockito.mock(Page.class)).when(repos)
                .findAllByUsernameStartingWith(Mockito.anyString(), Mockito.any(Pageable.class));

        // Act
        try {
            service.search("testuser", PageRequest.of(0, 20));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"spring-nextjs.userProfile:read"})
    void test_search_withAnyPermission() {
        // Arrange
        Mockito.doReturn(Mockito.mock(Page.class)).when(repos)
                .findAllByUsernameStartingWith(Mockito.anyString(), Mockito.any(Pageable.class));

        // Act
        try {
            service.search("testuser", PageRequest.of(0, 20));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /* lookup */

    @Test
    void test_lookup_withUnauthorized() {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findById(Mockito.anyInt());

        // Act
        try {
            service.lookup(9000);
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithAnonymousUser
    void test_lookup_withAnonymousUser() {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findById(Mockito.anyInt());

        // Act
        try {
            service.lookup(9000);
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"spring-nextjs.userProfile:read"})
    void test_lookup_withAnyPermission() {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findById(Mockito.anyInt());

        // Act
        try {
            service.lookup(9000);
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /* lookupByUsername */

    @Test
    void test_lookupByUsername_withUnauthorized() {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findByUsername(Mockito.anyString());

        // Act
        try {
            service.lookupByUsername("otheruser");
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithAnonymousUser
    void test_lookupByUsername_withAnonymousUser() {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findByUsername(Mockito.anyString());

        // Act
        try {
            service.lookupByUsername("otheruser");
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"spring-nextjs.userProfile:read"})
    void test_lookupByUsername_withAnyPermission() {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findByUsername(Mockito.anyString());

        // Act
        try {
            service.lookupByUsername("otheruser");
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /* create */

    @Test
    void test_create_withUnauthorized() {
        // Arrange
        Mockito.doReturn(Optional.empty()).when(repos).findById(Mockito.anyInt());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.create(testUserProfile(9000,"testuser"));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithAnonymousUser
    void test_create_withAnonymousUser() {
        // Arrange
        Mockito.doReturn(Optional.empty()).when(repos).findById(Mockito.anyInt());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.create(testUserProfile(9000, "testuser"));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"spring-nextjs.userProfile:read"})
    void test_create_withReadPermission() {
        // Arrange
        Mockito.doReturn(Optional.empty()).when(repos).findById(Mockito.anyInt());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.create(testUserProfile(9000, "testuser"));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"spring-nextjs.userProfile:write"})
    void test_create_withWritePermission() {
        // Arrange
        Mockito.doReturn(Optional.empty()).when(repos).findById(Mockito.anyInt());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.create(testUserProfile(9000, "testuser"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"admin"})
    void test_create_withAdminPermission() {
        // Arrange
        Mockito.doReturn(Optional.empty()).when(repos).findById(Mockito.anyInt());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.create(testUserProfile(9000, "testuser"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    /* updateDiffByUsername */

    @Test
    void test_updateDiffByUsername_withUnauthenticated() throws Throwable {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "testuser"))).when(repos).findByUsername(Mockito.anyString());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.updateDiffByUsername("testuser", testUserProfileJson(9000, "changeuser"));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithAnonymousUser
    void test_updateDiffByUsername_withAnonymous() throws Throwable {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "testuser"))).when(repos).findByUsername(Mockito.anyString());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.updateDiffByUsername("testuser", testUserProfileJson(9000, "changeuser"));
            Assertions.fail();
        } catch (Exception e) {
            // Test OK
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {"admin"})
    void test_updateDiffByUsername_withAdminPermission() throws Throwable {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findByUsername(Mockito.anyString());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.updateDiffByUsername("otheruser", testUserProfileJson(9000, "changeuser"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    @WithMockUser(username = "admin", authorities = {})
    void test_updateDiffByUsername_withAdminUser() throws Throwable {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "otheruser"))).when(repos).findByUsername(Mockito.anyString());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.updateDiffByUsername("otheruser", testUserProfileJson(9000, "changeuser"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = {})
    void test_updateDiffByUsername_withSelfUser() throws Throwable {
        // Arrange
        Mockito.doReturn(Optional.of(testUserProfile(9000, "testuser"))).when(repos).findByUsername(Mockito.anyString());
        Mockito.doReturn(Mockito.mock(UserProfile.class)).when(repos).save(Mockito.any(UserProfile.class));

        // Act
        try {
            service.updateDiffByUsername("testuser", testUserProfileJson(9000, "changeuser"));
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    UserProfile testUserProfile(int id, String username) {
        return new UserProfile(
                id, username, "Familyname", "Givenname", 10, "Male", "Profile");
    }

    String testUserProfileJson(int id, String username) {
        return objectMapper.writeValueAsString(new UserProfile(
                id, username, "Familyname", "Givenname", 10, "Male", "Profile"));
    }
}
