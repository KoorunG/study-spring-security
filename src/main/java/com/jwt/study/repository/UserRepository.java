package com.jwt.study.repository;

import com.jwt.study.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * PackageName : com.jwt.study.repository
 * FileName : UserRepository
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
