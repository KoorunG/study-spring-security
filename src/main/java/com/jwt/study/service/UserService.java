package com.jwt.study.service;

import com.jwt.study.model.User;
import com.jwt.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PackageName : com.jwt.study.service
 * FileName : UserService
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void join(User user) {
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);

        user.setPassword(encPassword);
        user.setRoles("ROLE_USER");
        userRepository.save(user);
    }
}

