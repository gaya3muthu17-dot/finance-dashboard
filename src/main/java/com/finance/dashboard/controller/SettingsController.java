package com.finance.dashboard.controller;

import com.finance.dashboard.entity.User;
import com.finance.dashboard.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController {

    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "uploads/profiles/";

    public SettingsController(UserService userService, PasswordEncoder passwordEncoder) {
        super(userService);
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String settings(Model model) {
        model.addAttribute("activePage", "settings");
        return "settings/index";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam(required=false) String phone,
                                @RequestParam(value="photo", required=false) MultipartFile photo,
                                Authentication auth,
                                RedirectAttributes ra) throws IOException {
        User user = userService.getByEmail(auth.getName()).orElse(null);
        if (user == null) return "redirect:/settings";

        user.setName(name);
        user.setEmail(email);
        if (phone != null) user.setPhone(phone);

        if (photo != null && !photo.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            String filename = user.getId() + "_" + photo.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            Files.copy(photo.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            user.setProfilePic(filename);
        }

        userService.updateProfile(user);
        ra.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/settings";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 Authentication auth,
                                 RedirectAttributes ra) {
        User user = userService.getByEmail(auth.getName()).orElse(null);
        if (user == null) return "redirect:/settings";

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            ra.addFlashAttribute("pwdError", "Current password is incorrect.");
            return "redirect:/settings";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateProfile(user);
        ra.addFlashAttribute("pwdSuccess", "Password changed successfully.");
        return "redirect:/settings";
    }
}
