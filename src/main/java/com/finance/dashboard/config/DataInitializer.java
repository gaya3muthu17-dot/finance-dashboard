package com.finance.dashboard.config;

import com.finance.dashboard.entity.*;
import com.finance.dashboard.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final OnboardingRepository onboardingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository ur, TransactionRepository tr,
                           OnboardingRepository or, PasswordEncoder pe) {
        this.userRepository = ur; this.transactionRepository = tr;
        this.onboardingRepository = or; this.passwordEncoder = pe;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@finance.com")) {
            User admin = new User();
            admin.setName("Admin User"); admin.setEmail("admin@finance.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN); admin.setStatus(User.Status.ACTIVE);
            userRepository.save(admin);

            // Mark admin onboarding as complete so it goes straight to dashboard
            OnboardingData od = new OnboardingData();
            od.setUser(admin); od.setCompleted(true);
            od.setIncomeSource("SALARY"); od.setMonthlyIncome(new BigDecimal("50000"));
            onboardingRepository.save(od);

            String[] titles = {"Product Sales","Office Rent","Consulting Revenue","Marketing Spend","Service Income","Utilities","Investment Return","Travel Expense"};
            Transaction.Type[] types = {Transaction.Type.INCOME,Transaction.Type.EXPENSE,Transaction.Type.INCOME,Transaction.Type.EXPENSE,Transaction.Type.INCOME,Transaction.Type.EXPENSE,Transaction.Type.INCOME,Transaction.Type.EXPENSE};
            String[] cats = {"Sales","Office","Services","Marketing","Services","Utilities","Investments","Travel"};
            BigDecimal[] amts = {new BigDecimal("15000"),new BigDecimal("3500"),new BigDecimal("8000"),new BigDecimal("2000"),new BigDecimal("5000"),new BigDecimal("800"),new BigDecimal("12000"),new BigDecimal("1200")};

            for (int i = 0; i < titles.length; i++) {
                Transaction t = new Transaction();
                t.setTitle(titles[i]); t.setAmount(amts[i]); t.setType(types[i]);
                t.setCategory(cats[i]); t.setDate(LocalDate.now().minusDays((long)i*5));
                t.setStatus(Transaction.Status.COMPLETED); t.setUser(admin);
                transactionRepository.save(t);
            }
        }
    }
}
