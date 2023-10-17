package com.wangxu.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class GwCorsFilter {
    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
 
        //cors跨域配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);//是否允许携带cookie
        // corsConfiguration.addAllowedOriginPattern("*");
        //corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedOrigin("*"); //允许跨域访问的域名，可填写具体域名，*代表允许所有访问
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        corsConfiguration.addAllowedMethod(HttpMethod.POST);//允许访问类型：get  post 等，*代表所有类型
        corsConfiguration.addAllowedMethod(HttpMethod.GET);
        corsConfiguration.addAllowedMethod(HttpMethod.HEAD);
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfiguration.addAllowedMethod(HttpMethod.PATCH);

        source.registerCorsConfiguration("/**", corsConfiguration);
 
        return new CorsWebFilter(source);
 
    }
}
