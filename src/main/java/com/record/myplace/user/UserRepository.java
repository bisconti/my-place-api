package com.record.myplace.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository를 상속받으면 기본 CRUD 메서드가 자동으로 제공됩니다.
// <엔티티 클래스, 엔티티 ID 타입>
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일을 기준으로 User 엔티티를 찾는 사용자 정의 메서드입니다.
    // Spring Data JPA가 메서드 이름 규칙을 보고 자동으로 SQL을 생성합니다.
    Optional<User> findByEmail(String email);
}
