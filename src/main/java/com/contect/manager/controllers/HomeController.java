package com.contect.manager.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.contect.manager.models.User;
import com.contect.manager.services.FileServices;
import com.contect.manager.services.UserServices;
import com.contect.manager.utils.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    FileServices fileServices;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "home");
        return "home";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "public/registerForm";
    }

    // We can configured to use our custom login page in securityConfig.java
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "public/loginForm";
    }

    @PostMapping("/submitRegisterForm")
    public String submitRegister(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(name = "terms", defaultValue = "false") boolean terms,
            @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session) {
        // Errors
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "public/registerForm";
        }
        if (!terms) {
            model.addAttribute("termsMessage", "Please accept Terms & Services to continue");
            return "public/registerForm";
        }

        // Process user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        try {
            String fileName = fileServices.saveFile(file, "/img/users/" + user.getEmail() + "/");
            if (fileName != null) {
                user.setImageName(fileName);
            } else {
                user.setImageName("default.png");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            user.setImageName("default.png");
        }

        // save to database
        userServices.saveUser(user);

        // Registration Success
        session.setAttribute("message", new Message("Registration Success", "success"));
        return "redirect:/login";
    }

    @GetMapping("/isUserExist/{username}")
    @ResponseBody
    public boolean isUserExist(@PathVariable("username") String username) {
        User user = userServices.getUserByUserName(username);
        if (user == null) {
            return false;
        }
        return true;
    }

    @GetMapping("/about")
    public String getAbout(Model model, HttpSession session, Principal principal) {
        /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            System.out.println(customUserDetails);
        } else {
            // Handle the case where the principal is not an instance of CustomUserDetails
            System.out.println("Principal is not a CustomUserDetails instance.");
        } 
        */
        session.setAttribute("message", new Message("this is content - ","danger"));
        return "public/about";
    }

    @GetMapping("/forgetPassword")
    public String forgetPassword() {
        return "public/forgetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("password") String newPassword,
            Model model, HttpSession session) {
        if (newPassword == null || newPassword.isEmpty()) {
            model.addAttribute("message", new Message("Password cannot be empty", "danger"));
            model.addAttribute("passwordError", "Password cannot be empty");
            return "public/forgetPassword";
        }
        try {
            User user = userServices.getUserByUserName(email);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userServices.saveUser(user);
                session.setAttribute("message", new Message("Password has been reset", "success"));
            } else {
                model.addAttribute("message", new Message("No user found with this email", "warning"));
                model.addAttribute("emailError", "No user found with this email");
                return "public/forgetPassword";
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.setAttribute("message", new Message("Failed to reset password. Try again", "danger"));
            return "public/forgetPassword";
        }
        return "redirect:/login";
    }
}
