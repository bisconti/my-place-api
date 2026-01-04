package com.record.myplace.user;

import java.time.LocalDate;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id 
    @Column(name = "useremail", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "username", nullable = false)
    private String username; 
    
    @Column(name = "birthDate")
    private LocalDate birthDate;
    
    @Column(name = "gender", length = 1)
    private String gender;
}