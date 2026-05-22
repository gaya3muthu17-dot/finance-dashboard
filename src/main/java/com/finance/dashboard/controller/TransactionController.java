package com.finance.dashboard.controller;

import com.finance.dashboard.entity.Transaction;
import com.finance.dashboard.service.DashboardService;
import com.finance.dashboard.service.TransactionService;
import com.finance.dashboard.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController extends BaseController {

    private final TransactionService transactionService;
    private final DashboardService dashboardService;

    public TransactionController(TransactionService ts, UserService us, DashboardService ds) {
        super(us);
        this.transactionService = ts;
        this.dashboardService = ds;
    }

    @GetMapping
    public String list(Model model, @RequestParam(required=false) String keyword,
                       @RequestParam(required=false) String type,
                       @RequestParam(required=false) String category,
                       @RequestParam(required=false) String startDate,
                       @RequestParam(required=false) String endDate) {
        List<Transaction> transactions = (keyword!=null||type!=null||category!=null||startDate!=null||endDate!=null)
            ? transactionService.search(keyword,type,category,startDate,endDate)
            : transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        model.addAttribute("totalRevenue", dashboardService.getTotalRevenue());
        model.addAttribute("totalExpense", dashboardService.getTotalExpense());
        model.addAttribute("activePage","transactions");
        return "transactions/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("activePage","transactions");
        return "transactions/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Transaction transaction, Authentication auth, RedirectAttributes ra) {
        userService.getByEmail(auth.getName()).ifPresent(u -> transaction.setUser(u));
        transactionService.save(transaction);
        ra.addFlashAttribute("success","Transaction saved successfully.");
        return "redirect:/transactions";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        transactionService.getById(id).ifPresent(t -> model.addAttribute("transaction",t));
        model.addAttribute("activePage","transactions");
        return "transactions/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        transactionService.delete(id);
        ra.addFlashAttribute("success","Transaction deleted.");
        return "redirect:/transactions";
    }
}
