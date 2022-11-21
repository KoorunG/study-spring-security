package com.jwt.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * PackageName : com.jwt.study.config
 * FileName : CorsConfig
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);       // 내 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*");           // 모든 ip에 응답을 허용
        config.addAllowedHeader("*");           // 모든 헤더에 응답을 허용
        config.addAllowedMethod("*");           // 모든 메소드에 응답을 허용
        source.registerCorsConfiguration("/api/**", config);    // 설정 등록 후
        return new CorsFilter(source);          // 리턴
    }
}
