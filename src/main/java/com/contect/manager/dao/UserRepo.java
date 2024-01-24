package com.contect.manager.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contect.manager.models.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    @Query("SELECT u from User u where u.email = :email")
    public User getUserByUserName(@Param("email") String email);
}