package com.contect.manager.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contect.manager.services.UserServices;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    
    @GetMapping("/home")
    public String home(Principal principal,Model model) {
        model.addAttribute("user", principal.getName());
        model.addAttribute("title", "HOME -Welcome");

        System.out.println(principal);
        return "user/home";
    }
}
