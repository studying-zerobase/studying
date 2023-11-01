package com.zerobase.munbanggu.repository;

import com.zerobase.munbanggu.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);


    Boolean existsByEmail(String email);
}
