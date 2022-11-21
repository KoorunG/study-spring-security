package com.jwt.study.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.study.auth.PrincipalDetails;
import com.jwt.study.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * PackageName : com.jwt.study.jwt
 * FileName : JwtAuthenticationFilter
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter는 /login 요청하여 username, password를 POST로 전송하면 동작한다.
// loginForm을 disable으로 설정했기 때문에 동작하지 않는 상태
// 다시 SecurityConfig에 addFilter로 등록해주면 다시 동작한다.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    // 여기에서 id, pw를 확인 후 로그인 시도를하면 PrincipalDetailsService의 loadUserByUsername() 함수가 실행된다.
    // PrincipalDetails를 세션에 담아(권한관리를 위해 세션에 담음) -> JWT Token을 만들어서 응답해주면 된다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도중 ::: JwtAuthenticationFilter");
        try {
            // mapper으로 User객체 생성
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(request.getInputStream(), User.class);
            // 유저정보로 토큰생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // PrincipalDetailsService의 loadUserByUsername 실행
            // DB에 있는 username과 password가 일치한다는 뜻
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            // 로그인이 정상적으로 되었다는 뜻
            System.out.println("로그인 완료됨 ::: " + principalDetails.getUser());
            // 리턴한 authentication 객체는 session 영역에 저장됨 -> security가 권한관리를 하게끔 세션에 authentication을 저장하는 것!
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 됐을 때 실행
    // 여기에서 JWT 토큰을 만들어서 요청한 클라이언트에게 JWT 토큰을 응답으로 전달해주면 됨!
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증이 완료됨 ::: successfulAuthentication");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // JWT 라이브러리를 이용하여 JWT 토큰 생성 (HMAC512)
        String jwtToken = JWT.create()
                .withSubject("jwt token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))     // 현재시간 + 토큰의 지속시간 (여기서는 10분)
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("koorung"));

        System.out.println("JWT ::: " + jwtToken);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
