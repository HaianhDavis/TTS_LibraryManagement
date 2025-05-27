package com.example.TTS_LibraryManagement.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Properties;

@Slf4j
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private final Properties roleProperties;
    private Object filterObject;
    private Object returnObject;
    private Object target;
    @Setter
    private HttpServletRequest request;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, Properties roleProperties) {
        super(authentication);
        this.roleProperties = roleProperties;
    }

    public boolean fileRole(HttpServletRequest httpServletRequest) {
        HttpServletRequest request = httpServletRequest != null ? httpServletRequest : getCurrentHttpRequest();
        if (request == null) {
            log.error("HttpServletRequest is null");
            return false;
        }

        String uri = request.getRequestURI(); // Ví dụ: /identity/api/v1/library/book
        String contextPath = request.getContextPath(); // Ví dụ: /identity/api/v1/library
        if (contextPath != null && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length()); // Loại bỏ context-path, còn: /book
        }
        uri = uri.replaceAll("^/+", "").replaceAll("/+$", ""); // Loại bỏ / đầu và cuối, còn: book
        uri = uri.replace("/", "."); // Thay / thành ., còn: book

        // Thêm prefix để khớp với roles.properties
        String normalizedUri = "api.v1.library." + uri; // Kết quả: api.v1.library.book

        log.debug("Normalized URI: {}", normalizedUri);
        String requiredPermission = roleProperties.getProperty(normalizedUri);
        if (requiredPermission == null) {
            log.warn("No permission found for URI: {}", normalizedUri);
            return false;
        }

        boolean hasAuthority = hasAuthority(requiredPermission);
        log.debug("Has authority [{}] for permission [{}]: {}", getAuthentication().getName(), requiredPermission, hasAuthority);
        return hasAuthority;
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }


    public void setThis(Object aThis) {
        this.target = aThis;
    }
}