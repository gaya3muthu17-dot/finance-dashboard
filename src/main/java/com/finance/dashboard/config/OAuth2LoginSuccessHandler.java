package com.finance.dashboard.config;

import com.finance.dashboard.entity.OnboardingData;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.OnboardingRepository;
import com.finance.dashboard.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;

    public OAuth2LoginSuccessHandler(UserRepository userRepository,
                                     OnboardingRepository onboardingRepository) {
        this.userRepository = userRepository;
        this.onboardingRepository = onboardingRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name  = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        // Auto-register if first time
        Optional<User> existing = userRepository.findByEmail(email);
        User user;
        if (existing.isEmpty()) {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword("GOOGLE_OAUTH");
            user.setProfilePic(picture);
            user.setRole(User.Role.STAFF);
            user.setStatus(User.Status.ACTIVE);
            userRepository.save(user);
        } else {
            user = existing.get();
            // Update profile pic from Google
            if (picture != null) {
                user.setProfilePic(picture);
                userRepository.save(user);
            }
        }

        // Check onboarding
        Optional<OnboardingData> onboarding = onboardingRepository.findByUser(user);
        if (onboarding.isEmpty() || !onboarding.get().isCompleted()) {
            response.sendRedirect("/onboarding/step1");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
}
