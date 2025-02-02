package com.anime_social.repository;

import com.anime_social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);
    public User findByFullName(String fullName);
}
