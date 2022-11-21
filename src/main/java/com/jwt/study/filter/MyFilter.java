package com.jwt.study.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * PackageName : com.jwt.study.filter
 * FileName : MyFilter
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */

// JWT가 어떤 방식으로 돌아가는지 간단히 하기 위한 커스텀 Filter
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(":::::::: 필터1");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        // 아이디, 패스워드가 정상적으로 들어와서 로그인이 완료될 때 "test" 토큰을 만들어서 응답에 넣어준다.
        // 클라이언트는 요청할때마다 header의 Authorization에 value값으로 토큰을 가져옴
        // 넘어온 토큰이 내가 만든 토큰 ("test")과 맞는지 검증하면 됨..
        if(req.getMethod().equals("GET")) {
            String authorization = req.getHeader("Authorization");
            System.out.println("authorization :::: " + authorization);

            if(authorization.equals("test")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }
    }
}
