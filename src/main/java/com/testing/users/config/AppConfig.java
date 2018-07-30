package com.testing.users.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@PropertySource("classpath:custom.properties")
@ConfigurationProperties(prefix="conf")
public class AppConfig {

    private String slash;
    private String borderSlash;
    private String wildcard;
    private List<Pattern> patterns = new ArrayList<>();

    public String getSlash() {
        return slash;
    }

    public void setSlash(String slash) {
        this.slash = slash;
    }

    public String getBorderSlash() {
        return borderSlash;
    }

    public void setBorderSlash(String borderSlash) {
        this.borderSlash = borderSlash;
    }

    public String getWildcard() {
        return wildcard;
    }

    public void setWildcard(String wildcard) {
        this.wildcard = wildcard;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public static class Pattern{


        String path;
        String method;
        String[] parts;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String[] getParts() {
            return parts;
        }

        public void setParts(String[] parts) {
            this.parts = parts;
        }
    }
}
