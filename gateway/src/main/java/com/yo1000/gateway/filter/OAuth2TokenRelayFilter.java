package com.yo1000.gateway.filter;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class OAuth2TokenRelayFilter implements GlobalFilter {

    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2ClientProperties oAuth2ClientProperties;

    public OAuth2TokenRelayFilter(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager,
            OAuth2ClientProperties oAuth2ClientProperties
    ) {
        this.authorizedClientManager = authorizedClientManager;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (oAuth2ClientProperties != null && oAuth2ClientProperties.getRegistration() != null
                && oAuth2ClientProperties.getRegistration().size() == 1) {
            String clientId = oAuth2ClientProperties.getRegistration().keySet().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Must configure at only 1 that ClientRegistrationId."));

            return Mono.deferContextual(Mono::just)
                    .flatMap(context -> {
                        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                                .withClientRegistrationId(clientId)
                                .principal("client_credentials")
                                .build();

                        return this.authorizedClientManager.authorize(authorizeRequest)
                                .flatMap(authorizedClient -> {
                                    exchange.getRequest()
                                            .mutate()
                                            .header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue());
                                    return chain.filter(exchange);
                                });
                    });
        } else {
            throw new IllegalStateException("Must configure at only 1 that ClientRegistrationId.");
        }
    }
}
