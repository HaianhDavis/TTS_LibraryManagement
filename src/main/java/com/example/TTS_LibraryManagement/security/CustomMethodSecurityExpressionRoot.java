package com.example.TTS_LibraryManagement.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Getter
@Setter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Properties roleProperties;
    private Object filterObject;
    private Object returnObject;
    private Object target;
    @Setter
    private HttpServletRequest request;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
        loadRoleProperties();
        log.info("Initializing CustomMethodSecurityExpressionRoot");
    }

    // Load role.properties file
    private void loadRoleProperties() {
        roleProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("role.properties")) {
            if (input != null) {
                roleProperties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load role.properties file", e);
        }
    }

    public boolean fileRole(HttpServletRequest httpServletRequest) {
        boolean hasRequiredScope = hasAuthority("ADMIN");
        if (hasRequiredScope) {
            return true;
        }
        log.info("Entering fileRole with HttpServletRequest: {}", httpServletRequest);
        try {
            if (Objects.isNull(httpServletRequest)) {
                log.warn("HttpServletRequest is null");
                return false;
            }

            String uri = httpServletRequest.getRequestURI();
            String contextPath = httpServletRequest.getContextPath();
            log.debug("Request URI: {}, Context Path: {}", uri, contextPath);

            if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
                uri = uri.substring(contextPath.length());
            }
            uri = uri.replaceAll("^/+", "").replaceAll("/+$", "");
            uri = uri.replace("/", ".");

            String normalizedUri = "api.v1.library." + uri;
            log.debug("Normalized URI: {}", normalizedUri);

            String requiredPermission = roleProperties.getProperty(normalizedUri);
            if (requiredPermission == null) {
                log.warn("No permission found for URI: {}", normalizedUri);
                return false;
            }

            log.debug("Required Permission: {}", requiredPermission);
            boolean hasAuthority = hasAuthority(requiredPermission);
            log.debug("Has authority [{}] for permission [{}]: {}",
                    getAuthentication() != null ? getAuthentication().getName() : "null",
                    requiredPermission, hasAuthority);
            return hasAuthority;
        } catch (Exception e) {
            log.error("Error in fileRole for URI: {}",
                    httpServletRequest != null ? httpServletRequest.getRequestURI() : "null", e);
            return false;
        }
    }


    @Override
    public Object getThis() {
        return null;
    }
}