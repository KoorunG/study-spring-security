package com.jwt.study.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PackageName : com.jwt.study.model
 * FileName : User
 * Author : Koorung
 * Date : 2022년 11월 21일
 * Description :
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String roles;       // Role은 여러개가 올 수 있는데 , 로 연결하여 String으로 반환한다.

    public List<String> getRoleList() {
        if(StringUtils.hasText(roles)) {
            String[] strings = roles.split(",");
            return Arrays.asList(strings);
        }
        return new ArrayList<>();
    }
}
