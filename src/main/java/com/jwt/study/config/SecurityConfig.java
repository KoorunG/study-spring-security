package com.jwt.study.config;

import com.jwt.study.jwt.JwtAuthenticationFilter;
import com.jwt.study.jwt.JwtAuthorizationFilter;
import com.jwt.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

/**
 * PackageName : com.jwt.study.config
 * FileName : SecurityConfig
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     // JWT를 사용할 예정이기 때문에 STATELESS 설정을 해줘야 한다!
                .and()
//                .addFilterBefore(new MyFilter(), WebAsyncManagerIntegrationFilter.class) // 시큐리티 필터 중 가장 먼저 동작하는 필터
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomConfigurer()) // Custom Configurer를 적용
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

        return http.build();
    }

    // corsFilter, JwtAuthenticationFilter 등등 커스텀 필터를 설정하는 커스텀 Configurer 구현
    public class CustomConfigurer extends AbstractHttpConfigurer<CustomConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter) // 인증이 필요한 요청을 포함한 모든 요청의 cors를 벗겨내는 필터 등록! (@CrossOrigin : 인증X인 경우에만 적용)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager)) // JwtAuthenticationFilter는 인자로 AuthenticationManager를 받는다.
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}
