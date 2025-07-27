package com.example.art_gal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để có thể truy cập file tĩnh qua URL /api/files/**
        // Nó sẽ ánh xạ URL này đến thư mục vật lý trên máy của bạn
        registry.addResourceHandler("/api/files/**")
                .addResourceLocations("file:" + uploadDir);
    }
}