//package com.example.TTS_LibraryManagement.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.security.access.expression.SecurityExpressionRoot;
//import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//@Component
//public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
//
//    private final ResourceLoader resourceLoader;
//    private Map<String, String> roleMappings;
//
//    @Autowired
//    public CustomMethodSecurityExpressionRoot(ResourceLoader resourceLoader) {
//        super((Authentication) null); // Không cần Authentication tại thời điểm khởi tạo
//        this.resourceLoader = resourceLoader;
//        this.roleMappings = new HashMap<>();
//        loadRoleMappings(); // Tải role từ file properties khi khởi tạo
//    }
//
//    // Phương thức tải role từ file role.properties
//    private void loadRoleMappings() {
//        try {
//            Resource resource = resourceLoader.getResource("role.properties");
//            Properties properties = new Properties();
//            properties.load(resource.getInputStream());
//            properties.forEach((key, value) -> roleMappings.put((String) key, (String) value));
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load role.properties", e);
//        }
//    }
//
//    // Phương thức kiểm tra quyền dựa trên URI
//    public boolean fileRole(HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false;
//        }
//
//        String uri = request.getRequestURI();
//        String requiredRole = roleMappings.getOrDefault(uri, null);
//        if (requiredRole == null) {
//            return false; // Không tìm thấy role cho URI, từ chối truy cập
//        }
//
//        return authentication.getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredRole));
//    }
//
//    @Override
//    public void setFilterObject(Object filterObject) {
//        // Không cần triển khai nếu không sử dụng filter
//    }
//
//    @Override
//    public Object getFilterObject() {
//        return null;
//    }
//
//    @Override
//    public void setReturnObject(Object returnObject) {
//        // Không cần triển khai nếu không sử dụng return object
//    }
//
//    @Override
//    public Object getReturnObject() {
//        return null;
//    }
//
//    @Override
//    public Object getThis() {
//        return this;
//    }
//}