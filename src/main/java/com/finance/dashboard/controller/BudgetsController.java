package com.finance.dashboard.controller;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/budgets")
public class BudgetsController extends BaseController {
    public BudgetsController(UserService us) { super(us); }
    @GetMapping
    public String budgets(Model model) { model.addAttribute("activePage","budgets"); return "budgets/index"; }
}
