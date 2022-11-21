package com.jwt.study.config;

import com.jwt.study.filter.MyFilter;
import com.jwt.study.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PackageName : com.jwt.study.config
 * FileName : FilterConfig
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<MyFilter> filter1() {
//        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>(new MyFilter());
//        bean.addUrlPatterns("/*");  // 모든 요청에 대해 필터를 적용
//        bean.setOrder(0);           // 필터가 실행되는 순서 정함 (0 : 가장 먼저)
//        return bean;
//    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");  // 모든 요청에 대해 필터를 적용
        bean.setOrder(1);
        return bean;
    }
}
