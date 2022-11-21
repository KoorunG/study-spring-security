package com.jwt.study.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * PackageName : com.jwt.study.filter
 * FileName : MyFilter2
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
public class MyFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(":::::::: 필터2");
        chain.doFilter(request, response);
    }
}

