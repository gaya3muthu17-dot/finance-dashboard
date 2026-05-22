package com.finance.dashboard.controller;

import com.finance.dashboard.service.DashboardService;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class DashboardController extends BaseController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService, UserService userService) {
        super(userService);
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/","/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalRevenue", dashboardService.getTotalRevenue());
        model.addAttribute("totalExpense", dashboardService.getTotalExpense());
        model.addAttribute("monthlyProfit", dashboardService.getMonthlyProfit());
        model.addAttribute("totalTransactions", dashboardService.getTotalTransactions());
        model.addAttribute("recentTransactions", dashboardService.getRecentTransactions());
        model.addAttribute("chartData", dashboardService.getMonthlyChartData(LocalDate.now().getYear()));
        model.addAttribute("activePage", "dashboard");
        return "dashboard/index";
    }
}
