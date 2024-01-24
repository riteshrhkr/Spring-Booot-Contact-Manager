package com.contect.manager.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contect.manager.config.CustomUserDetails;
import com.contect.manager.models.User;
import com.contect.manager.services.FileServices;
import com.contect.manager.services.UserServices;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private FileServices fileServices;

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("user", principal.getName());
        model.addAttribute("title", "HOME -Welcome");

        System.out.println(principal);
        return "user/home";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication.getPrincipal() instanceof UserDetails) {
        //     CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //     model.addAttribute("user", userDetails.getUser());
        // } else {
        //     model.addAttribute("user", userServices.getUserByUserName(principal.getName()));
        // }
        model.addAttribute("user", userServices.getUserByUserName(principal.getName()));
        /* In thymeleaf we can access user without sending from handler
         <div th:text = "${#authentication.principal.username}"></div>---> will print username
         <div th:text = "${#authentication.principal.user.email}"></div>---> will print username
        
         if user have not logged in yet then principal = annonymous as string and trying to access user throw error
        
        */
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User newUser,
            @RequestParam("profileImage") MultipartFile file, Model modal, Principal principal) {
        
        User oldUser = userServices.getUserByUserName(principal.getName());
        oldUser.setName(newUser.getName());
        oldUser.setAbout(newUser.getAbout());
        if (!file.isEmpty() && FileServices.isImage(file.getContentType())) {
            try {
                String fileName = fileServices.updateFile(file, "/img/users/" + oldUser.getEmail() + "/", oldUser.getImageName());
                if (fileName != null) {
                    oldUser.setImageName(fileName);
                } else {
                    oldUser.setImageName("default.png");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                oldUser.setImageName("default.png");
            }
        }
       newUser = userServices.saveUser(oldUser);
       System.out.println(newUser);
        return "redirect:/user/profile";
    }
}
