package com.contect.manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contect.manager.services.UserServices;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServices userServices;
    
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "HOME -Admin page");
        return "admin/home";
    }
}
