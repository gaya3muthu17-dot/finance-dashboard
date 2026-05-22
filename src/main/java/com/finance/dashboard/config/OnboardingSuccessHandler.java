package com.finance.dashboard.config;

import com.finance.dashboard.entity.OnboardingData;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.OnboardingRepository;
import com.finance.dashboard.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OnboardingSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;

    public OnboardingSuccessHandler(UserRepository userRepository, OnboardingRepository onboardingRepository) {
        this.userRepository = userRepository;
        this.onboardingRepository = onboardingRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<OnboardingData> onboarding = onboardingRepository.findByUser(user);
            if (onboarding.isEmpty() || !onboarding.get().isCompleted()) {
                response.sendRedirect("/onboarding/step1");
                return;
            }
        }
        response.sendRedirect("/dashboard");
    }
}
