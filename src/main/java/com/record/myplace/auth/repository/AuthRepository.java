package com.record.myplace.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.user.entity.User;

public interface AuthRepository extends JpaRepository<User, String> {
    // 이메일을 기준으로 User 엔티티를 찾는 사용자 정의 메서드입니다.
    // Spring Data JPA가 메서드 이름 규칙을 보고 자동으로 SQL을 생성합니다.
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
