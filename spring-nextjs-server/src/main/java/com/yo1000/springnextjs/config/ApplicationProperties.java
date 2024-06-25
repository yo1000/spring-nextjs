package com.yo1000.springnextjs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private SecurityProperties security;

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public static class SecurityProperties {
        private CorsProperties cors;

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
}
