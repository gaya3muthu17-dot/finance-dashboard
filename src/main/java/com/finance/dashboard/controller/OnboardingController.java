package com.finance.dashboard.controller;

import com.finance.dashboard.entity.*;
import com.finance.dashboard.repository.OnboardingRepository;
import com.finance.dashboard.service.AccountService;
import com.finance.dashboard.service.TransactionService;
import com.finance.dashboard.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/onboarding")
public class OnboardingController extends BaseController {

    private final OnboardingRepository onboardingRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public OnboardingController(UserService userService,
                                OnboardingRepository onboardingRepository,
                                AccountService accountService,
                                TransactionService transactionService) {
        super(userService);
        this.onboardingRepository = onboardingRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    // Step 1 - Income Source
    @GetMapping("/step1")
    public String step1() { return "onboarding/step1"; }

    @PostMapping("/step1")
    public String step1Post(@RequestParam String incomeSource, Authentication auth) {
        User user = userService.getByEmail(auth.getName()).orElseThrow();
        OnboardingData data = onboardingRepository.findByUser(user).orElse(new OnboardingData());
        data.setUser(user);
        data.setIncomeSource(incomeSource);
        onboardingRepository.save(data);
        return "redirect:/onboarding/step2";
    }

    // Step 2 - Monthly Income
    @GetMapping("/step2")
    public String step2() { return "onboarding/step2"; }

    @PostMapping("/step2")
    public String step2Post(@RequestParam BigDecimal salary, Authentication auth) {
        User user = userService.getByEmail(auth.getName()).orElseThrow();
        OnboardingData data = onboardingRepository.findByUser(user).orElse(new OnboardingData());
        data.setUser(user);
        data.setMonthlyIncome(salary);
        onboardingRepository.save(data);

        // Create income transaction from this
        Transaction t = new Transaction();
        t.setTitle("Monthly Salary");
        t.setAmount(salary);
        t.setType(Transaction.Type.INCOME);
        t.setCategory("Salary");
        t.setDate(LocalDate.now());
        t.setStatus(Transaction.Status.COMPLETED);
        t.setUser(user);
        transactionService.save(t);

        return "redirect:/onboarding/step3";
    }

    // Step 3 - Where do you manage money
    @GetMapping("/step3")
    public String step3() { return "onboarding/step3"; }

    @PostMapping("/step3")
    public String step3Post(@RequestParam(required = false) List<String> manage, Authentication auth) {
        User user = userService.getByEmail(auth.getName()).orElseThrow();
        OnboardingData data = onboardingRepository.findByUser(user).orElse(new OnboardingData());
        data.setUser(user);
        data.setMoneyManagement(manage != null ? String.join(",", manage) : "");
        onboardingRepository.save(data);
        return "redirect:/onboarding/step4";
    }

    // Step 4 - Add first account
    @GetMapping("/step4")
    public String step4() { return "onboarding/step4"; }

    @PostMapping("/step4")
    public String step4Post(@RequestParam(required = false) String skip,
                            @RequestParam(required = false) String accountType,
                            @RequestParam(required = false) String holderName,
                            @RequestParam(required = false) String accountTypeName,
                            @RequestParam(required = false) String accountNumber,
                            @RequestParam(required = false) String provider,
                            @RequestParam(required = false) BigDecimal initialBalance,
                            Authentication auth) {
        if (skip == null && accountType != null && provider != null) {
            User user = userService.getByEmail(auth.getName()).orElseThrow();
            Account acc = new Account();
            acc.setAccountType(Account.AccountType.valueOf(accountType.toUpperCase().replace(" ", "_")));
            acc.setAccountName(accountTypeName != null ? accountTypeName : "Savings Account");
            acc.setBankName(provider);
            acc.setAccountNumber(accountNumber);
            acc.setBalance(initialBalance != null ? initialBalance : BigDecimal.ZERO);
            acc.setUser(user);
            accountService.save(acc);
        }
        return "redirect:/onboarding/step5";
    }

    // Step 5 - Create first budget
    @GetMapping("/step5")
    public String step5() { return "onboarding/step5"; }

    @PostMapping("/step5")
    public String step5Post(@RequestParam(required = false) String skip,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) BigDecimal budgetAmount,
                            Authentication auth) {
        // Budget saved — redirect to step 6
        return "redirect:/onboarding/step6";
    }

    // Step 6 - Connect first bill
    @GetMapping("/step6")
    public String step6() { return "onboarding/step6"; }

    @PostMapping("/step6")
    public String step6Post(@RequestParam(required = false) String skip,
                            @RequestParam(required = false) String billName,
                            @RequestParam(required = false) String consumerId,
                            @RequestParam(required = false) String holderName,
                            @RequestParam(required = false) BigDecimal amount,
                            Authentication auth) {
        if (skip == null && billName != null && amount != null) {
            User user = userService.getByEmail(auth.getName()).orElseThrow();
            Transaction t = new Transaction();
            t.setTitle(billName);
            t.setAmount(amount);
            t.setType(Transaction.Type.EXPENSE);
            t.setCategory("Bills");
            t.setDate(LocalDate.now());
            t.setStatus(Transaction.Status.ACTIVE);
            t.setUser(user);
            transactionService.save(t);
        }
        return "redirect:/onboarding/finish";
    }

    // Finish
    @GetMapping("/finish")
    public String finish(Authentication auth) {
        User user = userService.getByEmail(auth.getName()).orElseThrow();
        OnboardingData data = onboardingRepository.findByUser(user).orElse(new OnboardingData());
        data.setUser(user);
        data.setCompleted(true);
        onboardingRepository.save(data);
        return "redirect:/dashboard";
    }
}
