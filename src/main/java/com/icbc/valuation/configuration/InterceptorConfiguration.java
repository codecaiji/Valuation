package com.icbc.valuation.configuration;

import com.icbc.valuation.configuration.interceptor.RequestHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    public static final String PATH_PATTERN = "/**";
    public static final String REQUEST_INTERCEPTOR_PATH_PATTERN = "/**/*";

    @Autowired
    RequestHandlerInterceptor requestHandlerInterceptor;

    /*@Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration(PATH_PATTERN, config);
        return new CorsFilter(configSource);
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHandlerInterceptor)
                .addPathPatterns(REQUEST_INTERCEPTOR_PATH_PATTERN);
    }
}
