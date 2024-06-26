package com.yo1000.springnextjs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private SecurityProperties security;
    private FrontendProperties frontend;

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public FrontendProperties getFrontend() {
        return frontend;
    }

    public void setFrontend(FrontendProperties frontend) {
        this.frontend = frontend;
    }

    public static class SecurityProperties {
        private String idp;
        private CorsProperties cors;

        public String getIdp() {
            return idp;
        }

        public void setIdp(String idp) {
            this.idp = idp;
        }

        public CorsProperties getCors() {
            return cors;
        }

        public void setCors(CorsProperties cors) {
            this.cors = cors;
        }

        public static class CorsProperties {
            private List<String> allowedOrigins;

            public List<String> getAllowedOrigins() {
                return allowedOrigins;
            }

            public void setAllowedOrigins(List<String> allowedOrigins) {
                this.allowedOrigins = allowedOrigins;
            }
        }
    }

    public static class FrontendProperties {
        private OidcProperties oidc;

        public OidcProperties getOidc() {
            return oidc;
        }

        public void setOidc(OidcProperties oidc) {
            this.oidc = oidc;
        }

        public static class OidcProperties {
            private String authority;
            private String clientId;
            private String redirectUri;
            private String postLogoutRedirectUri;

            public String getAuthority() {
                return authority;
            }

            public void setAuthority(String authority) {
                this.authority = authority;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getRedirectUri() {
                return redirectUri;
            }

            public void setRedirectUri(String redirectUri) {
                this.redirectUri = redirectUri;
            }

            public String getPostLogoutRedirectUri() {
                return postLogoutRedirectUri;
            }

            public void setPostLogoutRedirectUri(String postLogoutRedirectUri) {
                this.postLogoutRedirectUri = postLogoutRedirectUri;
            }
        }
    }
}
