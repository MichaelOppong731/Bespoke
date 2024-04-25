package com.bespoke.Bespoke.repository;

import com.bespoke.Bespoke.entities.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken,Integer> {

    ForgotPasswordToken findByToken(String token);
}
