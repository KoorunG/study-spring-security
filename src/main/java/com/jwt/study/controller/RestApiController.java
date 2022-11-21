package com.jwt.study.controller;

import com.jwt.study.auth.PrincipalDetails;
import com.jwt.study.model.User;
import com.jwt.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * PackageName : com.jwt.study.controller
 * FileName : RestApiController
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final UserService userService;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        userService.join(user);
        return "회원가입완료";
    }

    @GetMapping("/api/v1/user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication ::: " + principal.getUsername());
        return "유저권한";
    }

    @GetMapping("/test")
    public String test() {
        return "테스트";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "매니저권한";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "어드민권한";
    }
}
