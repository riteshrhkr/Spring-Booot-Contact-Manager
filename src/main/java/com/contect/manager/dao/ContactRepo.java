package com.contect.manager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contect.manager.models.Contact;
import com.contect.manager.models.User;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

    List<Contact> findByUser(User user);

    @Query("SELECT c FROM Contact c WHERE c.user.email = :email")
    Page<Contact> findByUsername(String email, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(c.user.email) = LOWER(:email)")
    List<Contact> findByNameAndUsername(@Param("name") String name, @Param("email") String username, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE LOWER(c.phone) LIKE LOWER(CONCAT('%', :phone, '%')) AND LOWER(c.user.email) = LOWER(:email)")
    List<Contact> findByPhoneAndUsername(@Param("phone") String name, @Param("email") String username, Pageable pageable);

}
