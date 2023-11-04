package com.zerobase.munbanggu.user.repository;

import com.zerobase.munbanggu.user.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
