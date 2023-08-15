package com.icbc.valuation.configuration;

import com.icbc.valuation.configuration.interceptor.RequestHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    public static final String PATH_PATTERN = "/**";
    public static final String REQUEST_INTERCEPTOR_PATH_PATTERN = "/**/*";

    @Autowired
    RequestHandlerInterceptor requestHandlerInterceptor;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://192.168.100.164:3000/");
        config.addAllowedOrigin("http://192.168.43.146:3000/");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration(PATH_PATTERN, config);
        return new CorsFilter(configSource);
    }
   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:12345") // 允许所有域的请求访问（生产环境中建议根据实际情况指定允许的域）
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestHandlerInterceptor)
                .addPathPatterns(REQUEST_INTERCEPTOR_PATH_PATTERN);
    }
}
