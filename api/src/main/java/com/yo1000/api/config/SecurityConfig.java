package com.yo1000.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(ApplicationProperties.class)
public class SecurityConfig {
    private final ApplicationProperties props;

    public SecurityConfig(ApplicationProperties props) {
        this.props = props;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity httpSecurity
    ) throws Exception {
        httpSecurity
                .oauth2ResourceServer(config -> config.jwt(Customizer.withDefaults()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        if (props.getSecurity() != null && props.getSecurity().getCors() != null && props.getSecurity().getCors().getAllowedOrigins() != null) {
            configuration.setAllowedOrigins(props.getSecurity().getCors().getAllowedOrigins());
        }

        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // for Keycloak
    @Bean
    @ConditionalOnProperty(name = "app.security.idp", havingValue = "keycloak")
    public JwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        Converter<Jwt, Collection<GrantedAuthority>> converter = jwt -> {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            List<String> realmRoles = (List<String>) realmAccess.get("roles");

            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            List<String> resourceRoles = resourceAccess.entrySet().stream()
                    .flatMap(entry -> ((Map<String, List<String>>) entry.getValue()).get("roles").stream()
                            .map(name -> entry.getKey() + "." + name))
                    .toList();

            Collection<GrantedAuthority> keycloakRoles = Stream.concat(realmRoles.stream(), resourceRoles.stream())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toCollection(ArrayList::new));

            return keycloakRoles;
        };

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
        return jwtAuthenticationConverter;
    }
}
