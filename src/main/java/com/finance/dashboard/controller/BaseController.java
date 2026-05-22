package com.finance.dashboard.controller;

import com.finance.dashboard.entity.User;
import com.finance.dashboard.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class BaseController {

    protected final UserService userService;

    public BaseController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addCurrentUser(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            userService.getByEmail(auth.getName()).ifPresent(u -> model.addAttribute("currentUser", u));
        }
    }
}
