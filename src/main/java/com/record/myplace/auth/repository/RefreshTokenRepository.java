package com.record.myplace.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.record.myplace.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	Optional<RefreshToken> findByTokenHashAndRevoked(String tokenHash, String revoked);
	List<RefreshToken> findAllByUseremailAndRevoked(String useremail, String revoked);
}
