package com.finance.dashboard.repository;

import com.finance.dashboard.entity.OnboardingData;
import com.finance.dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OnboardingRepository extends JpaRepository<OnboardingData, Long> {
    Optional<OnboardingData> findByUser(User user);
}
