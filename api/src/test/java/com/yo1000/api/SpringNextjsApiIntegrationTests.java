package com.yo1000.api;

import com.yo1000.api.domain.model.Item;
import com.yo1000.api.domain.model.ItemInventory;
import com.yo1000.api.domain.model.UserProfile;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public class SpringNextjsApiIntegrationTests {
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres"));

    @BeforeAll
    static void startContainers() {
        postgresContainer.start();
    }

    @Autowired
    MockMvcTester mockMvc;

    @Autowired
    EntityManager entityManager;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgresContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());

        // Required to setup DB.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.show-sql", () -> true);

        registry.add("app.security.idp", () -> "test");
    }

    @Test
    @WithMockUser(username = "test", authorities = {})
    void test_itemsApi() {
        entityManager.persist(new Item(9001, "Test1", 1000, 500));
        entityManager.persist(new Item(9002, "Test2", 2000, 1000));
        entityManager.persist(new Item(9003, "Test3", 3000, 1500));
        entityManager.persist(new Item(9004, "Test4", 4000, 2000));
        entityManager.persist(new Item(9005, "Demo5", 5000, 2500));
        entityManager.persist(new Item(9006, "Demo6", 6000, 3000));
        entityManager.persist(new Item(9007, "Demo7", 7000, 3500));
        entityManager.persist(new Item(9008, "Demo8", 8000, 4000));

        mockMvc.get().uri("/items")
                .param("name", "Test")
                .param("page", "0")
                .param("size", "3")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.totalElements", assertProvider -> assertProvider
                        .assertThat().isEqualTo(4))
                .hasPathSatisfying("$.number", assertProvider -> assertProvider
                        .assertThat().isEqualTo(0))
                .hasPathSatisfying("$.size", assertProvider -> assertProvider
                        .assertThat().isEqualTo(3))
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
                        .containsEntry("sellPrice", 1000))
                .hasPathSatisfying("$.content[2]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9003)
                        .containsEntry("name", "Test3")
                        .containsEntry("price", 3000)
                        .containsEntry("sellPrice", 1500));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"admin"})
    void test_itemInventoriesApi() {
        Optional.of(new Item(9001, "Test1", 1000, 500)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9001, item, 1));
        });
        Optional.of(new Item(9002, "Test2", 2000, 1000)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9002, item, 2));
        });
        Optional.of(new Item(9003, "Test3", 3000, 1500)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9003, item, 3));
        });
        Optional.of(new Item(9004, "Test4", 4000, 2000)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9004, item, 4));
        });
        Optional.of(new Item(9005, "Demo5", 5000, 2500)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9005, item, 5));
        });
        Optional.of(new Item(9006, "Demo6", 6000, 3000)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9006, item, 6));
        });
        Optional.of(new Item(9007, "Demo7", 7000, 3500)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9007, item, 7));
        });
        Optional.of(new Item(9008, "Demo8", 8000, 4000)).ifPresent(item -> {
            entityManager.persist(item);
            entityManager.persist(new ItemInventory(9008, item, 8));
        });

        mockMvc.get().uri("/itemInventories")
                .param("itemName", "Test")
                .param("page", "0")
                .param("size", "3")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.totalElements", assertProvider -> assertProvider
                        .assertThat().isEqualTo(4))
                .hasPathSatisfying("$.number", assertProvider -> assertProvider
                        .assertThat().isEqualTo(0))
                .hasPathSatisfying("$.size", assertProvider -> assertProvider
                        .assertThat().isEqualTo(3))
                .hasPathSatisfying("$.content[0]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9001)
                        .containsEntry("item", Map.ofEntries(
                                Map.entry("id", 9001),
                                Map.entry("name", "Test1"),
                                Map.entry("price", 1000),
                                Map.entry("sellPrice", 500)
                        ))
                        .containsEntry("quantity", 1))
                .hasPathSatisfying("$.content[1]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9002)
                        .containsEntry("item", Map.ofEntries(
                                Map.entry("id", 9002),
                                Map.entry("name", "Test2"),
                                Map.entry("price", 2000),
                                Map.entry("sellPrice", 1000)
                        ))
                        .containsEntry("quantity", 2))
                .hasPathSatisfying("$.content[2]", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9003)
                        .containsEntry("item", Map.ofEntries(
                                Map.entry("id", 9003),
                                Map.entry("name", "Test3"),
                                Map.entry("price", 3000),
                                Map.entry("sellPrice", 1500)
                        ))
                        .containsEntry("quantity", 3));
    }

    @Test
    @WithUserProfilePrincipal(
            id = 9002, username = "test2",
            familyName = "Family2", givenName = "Given2",
            age = 22, gender = "Female", profile = "Profile2"
    )
    void test_userProfilesApi() {
        entityManager.persist(new UserProfile(9001, "test1", "Family1", "Given1", 21, "Male", "Profile1"));
        entityManager.persist(new UserProfile(9002, "test2", "Family2", "Given2", 22, "Female", "Profile2"));
        entityManager.persist(new UserProfile(9003, "test3", "Family3", "Given3", 23, "Male", "Profile3"));
        entityManager.persist(new UserProfile(9004, "test4", "Family4", "Given4", 24, "Female", "Profile4"));

        mockMvc.patch().uri("/userProfiles")
                .param("username", "test2")
                .contentType("application/merge-patch+json")
                .content("""
                        {
                            "givenName": "UpdateGiven2",
                            "profile": "UpdateProfile2"
                        }
                        """)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$", assertProvider -> assertProvider
                        .assertThat()
                        .asMap()
                        .containsEntry("id", 9002)
                        .containsEntry("username", "test2")
                        .containsEntry("familyName", "Family2")
                        .containsEntry("givenName", "UpdateGiven2")
                        .containsEntry("age", 22)
                        .containsEntry("gender", "Female")
                        .containsEntry("profile", "UpdateProfile2"));
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @WithSecurityContext(factory = WithUserProfilePrincipalSecurityContextFactory.class)
    @interface WithUserProfilePrincipal {
        int id();
        String username();
        String familyName();
        String givenName();
        int age();
        String gender();
        String profile();
    }

    static class WithUserProfilePrincipalSecurityContextFactory implements WithSecurityContextFactory<WithUserProfilePrincipal> {
        @Override
        public SecurityContext createSecurityContext(WithUserProfilePrincipal annotation) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(new Authentication() {
                @Override
                public boolean isAuthenticated() {
                    return true;
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                    // NOP
                }

                @Override
                public String getName() {
                    return annotation.username();
                }

                @Override
                public Object getDetails() {
                    return new UserProfile(
                            annotation.id(),
                            annotation.username(),
                            annotation.familyName(),
                            annotation.givenName(),
                            annotation.age(),
                            annotation.gender(),
                            annotation.profile());
                }

                @Override
                public Object getPrincipal() {
                    return getDetails();
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of();
                }

                @Override
                public Object getCredentials() {
                    return null;
                }
            });
            return context;
        }
    }
}
