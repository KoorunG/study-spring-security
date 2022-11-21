package com.jwt.study.auth;

import com.jwt.study.model.User;
import com.jwt.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * PackageName : com.jwt.study.auth
 * FileName : PrincipalDetailsService
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description : /login 요청이 오면 호출됨
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        return new PrincipalDetails(user);
    }
}
