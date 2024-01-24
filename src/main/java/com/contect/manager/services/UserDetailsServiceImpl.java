package com.contect.manager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contect.manager.config.CustomUserDetails;
import com.contect.manager.dao.UserRepo;
import com.contect.manager.models.User;

// We are using email as username
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Cannot find user with " + username);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        
        return customUserDetails;
    }

}
