package com.contect.manager.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contect.manager.dao.UserRepo;
import com.contect.manager.models.User;

// For now, we are using email as username

@Service
public class UserServices {
    @Autowired
    private UserRepo userRepo;

    public User saveUser(User user) {
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            // Log and handle the exception as per your logging framework or strategy
            System.out.println("Exception occurred while saving the user: " + e.getMessage());
            return null;
        }
    }

    public User getUserByUserName(String username) {
        try {
            return userRepo.getUserByUserName(username);
        } catch (Exception e) {
            // Log and handle the exception as per your logging framework or strategy
            System.out.println("Exception occurred while finding the user by username: " + e.getMessage());
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepo.findAll();
        } catch (Exception e) {
            // Log and handle the exception as per your logging framework or strategy
            System.out.println("Exception occurred while retrieving all users: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public User getUserById(int id) {
        try {
            if (userRepo.existsById(id)) {
                return userRepo.findById(id).get();
            }
            return null;
        } catch (Exception e) {
            // Log and handle the exception as per your logging framework or strategy
            System.out.println("Exception occurred while retrieving the user: " + e.getMessage());
            return null;
        }
    }

    public User updateUser(int id, User updatedUser) {
        try {
            if (userRepo.existsById(id)) {
                updatedUser.setId(id);
                return userRepo.save(updatedUser);
            }
            return null;
        } catch (Exception e) {
            // Log and handle the exception as per your logging framework or strategy
            System.out.println("Exception occurred while updating the user: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteUserById(int id) {
        try {
            userRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            // Log and handle the exception as per your logging framework or strategy
            System.out.println("Exception occurred while deleting the user: " + e.getMessage());
            return false;
        }
    }
}
