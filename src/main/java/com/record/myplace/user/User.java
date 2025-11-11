package com.record.myplace.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Lombok을 사용하여 Getter와 Setter를 자동 생성합니다.
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    // 🔑 email을 기본 키(Primary Key)로 설정합니다.
    @Id 
    @Column(name = "useremail", nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // 실제 비밀번호 대신 해시된 비밀번호를 저장합니다.
    
    // 추가: SQL 명세와 일치시키기 위해 username 필드를 추가합니다.
    @Column(nullable = false)
    private String username; 
}