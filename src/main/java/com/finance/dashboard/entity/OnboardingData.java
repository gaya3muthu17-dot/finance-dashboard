package com.finance.dashboard.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "onboarding_data")
public class OnboardingData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne @JoinColumn(name = "user_id")
    private User user;

    private boolean completed = false;

    // Step 1 - income source
    private String incomeSource; // SALARY or BUSINESS

    // Step 2 - monthly income
    private BigDecimal monthlyIncome;

    // Step 3 - money management (comma separated)
    private String moneyManagement;

    // Step 4 - account added via accounts flow

    // Step 5 - budget added via budgets flow

    // Step 6/7 - bill added via bills flow

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getIncomeSource() { return incomeSource; }
    public void setIncomeSource(String incomeSource) { this.incomeSource = incomeSource; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public String getMoneyManagement() { return moneyManagement; }
    public void setMoneyManagement(String moneyManagement) { this.moneyManagement = moneyManagement; }
}
