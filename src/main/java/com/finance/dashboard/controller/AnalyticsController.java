package com.finance.dashboard.controller;
import com.finance.dashboard.service.DashboardService;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller @RequestMapping("/analytics")
public class AnalyticsController extends BaseController {
    private final DashboardService dashboardService;
    public AnalyticsController(DashboardService ds, UserService us) { super(us); this.dashboardService = ds; }
    @GetMapping
    public String analytics(@RequestParam(defaultValue="0") int year, Model model) {
        if (year==0) year = LocalDate.now().getYear();
        model.addAttribute("chartData", dashboardService.getMonthlyChartData(year));
        model.addAttribute("year", year);
        model.addAttribute("activePage","analytics");
        return "analytics/index";
    }
}
