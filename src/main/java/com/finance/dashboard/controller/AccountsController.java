package com.finance.dashboard.controller;

import com.finance.dashboard.entity.Account;
import com.finance.dashboard.service.AccountService;
import com.finance.dashboard.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
public class AccountsController extends BaseController {

    private final AccountService accountService;

    public AccountsController(AccountService accountService, UserService userService) {
        super(userService);
        this.accountService = accountService;
    }

    @GetMapping
    public String accounts(Model model, Authentication auth) {
        userService.getByEmail(auth.getName()).ifPresent(u ->
            model.addAttribute("accounts", accountService.getByUser(u))
        );
        model.addAttribute("activePage","accounts");
        model.addAttribute("newAccount", new Account());
        return "accounts/index";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Account account, Authentication auth, RedirectAttributes ra) {
        userService.getByEmail(auth.getName()).ifPresent(u -> account.setUser(u));
        accountService.save(account);
        ra.addFlashAttribute("success", "Account linked successfully.");
        return "redirect:/accounts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        accountService.delete(id);
        ra.addFlashAttribute("success", "Account removed.");
        return "redirect:/accounts";
    }
}
