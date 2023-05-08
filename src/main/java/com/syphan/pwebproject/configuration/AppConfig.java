package com.syphan.pwebproject.configuration;

import com.syphan.pwebproject.constants.PathConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor())
                .excludePathPatterns(
                        "/login",
                        PathConstants.LOGIN,
                        PathConstants.FORGOT_PASSWORD,
                        "/actuator/**",
                        "/erro", "/img/**", "/css/**", "/js/**",
                        "/logout"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}