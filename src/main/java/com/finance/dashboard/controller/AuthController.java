package com.finance.dashboard.controller;

import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.OnboardingRepository;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final OnboardingRepository onboardingRepository;

    public AuthController(UserService userService, OnboardingRepository onboardingRepository) {
        this.userService = userService;
        this.onboardingRepository = onboardingRepository;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required=false) String error,
                        @RequestParam(required=false) String logout, Model model) {
        if (error != null) model.addAttribute("error", true);
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupForm() { return "auth/signup"; }

    @PostMapping("/signup")
    public String signup(@RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam(required=false) String phone,
                         RedirectAttributes ra) {
        if (userService.existsByEmail(email)) {
            ra.addFlashAttribute("error", "Email already registered. Please login.");
            return "redirect:/auth/signup";
        }
        User u = new User();
        u.setName(name); u.setEmail(email); u.setPassword(password);
        if (phone != null && !phone.isBlank()) u.setPhone(phone);
        u.setRole(User.Role.STAFF); u.setStatus(User.Status.ACTIVE);
        userService.save(u);
        ra.addFlashAttribute("message", "Account created! Please login.");
        return "redirect:/auth/login";
    }

    @GetMapping("/logged-out")
    public String loggedOut() { return "auth/logout"; }
}
