package com.jwt.study.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jwt.study.auth.PrincipalDetails;
import com.jwt.study.model.User;
import com.jwt.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PackageName : com.jwt.study.jwt
 * FileName : JwtAuthorizationFilter
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */

// 시큐리티가 가지고 있는 filter중 BasicAuthenticationFilter라는게 있음
// 토큰이 있는지 없는지를 검사하는 필터
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader ::: " + jwtHeader);

        // jwtHeader 검증
        if(!StringUtils.hasText(jwtHeader) || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        // Bearer  을 제거하여 jwtToken 반환
        String jwtToken = jwtHeader.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("koorung")).build().verify(jwtToken);
        String username = decodedJWT.getClaim("username").asString();

        System.out.println("username ::: "+ username);

        // 서명이 정상적으로 됐으면
        if(StringUtils.hasText(username)) {
            // DB에서 user 정보를 조회한다.
            User user = userRepository.findByUsername(username).orElseThrow();

            PrincipalDetails principalDetails = new PrincipalDetails(user);

            // UsernamePasswordAuthenticationToken를 이용하여 강제적으로 Authentication 를 만들어준 것
            // 위에서 정상적으로 서명이 완료된 경우까지 검증했기 때문에 credentials에 null을 전달하는 것이 가능하다. (원래는 principalDetails.getPassword()가 들어가야함)
            // 중요) 세번째 인자로 authorities 를 전달해야 세션에 저장된 authentication를 참조하여 권한에 따른 URL 제어가 가능하다!!!
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 시큐리티의 세션공간
            SecurityContext securityContext = SecurityContextHolder.getContext();
            // 강제로 시큐리티의 세션공간에 Authentication를 세팅
            securityContext.setAuthentication(authentication);


            chain.doFilter(request, response);
        }
    }
}
