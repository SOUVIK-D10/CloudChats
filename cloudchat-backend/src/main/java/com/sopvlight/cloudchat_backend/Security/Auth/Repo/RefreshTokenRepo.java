package com.sopvlight.cloudchat_backend.Security.Auth.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sopvlight.cloudchat_backend.Security.Auth.Model.RefreshToken;
@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer>{
    RefreshToken findByToken(String token);
}
