package com.finance.dashboard.controller;
import com.finance.dashboard.service.TransactionService;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("/reports")
public class ReportsController extends BaseController {
    private final TransactionService transactionService;
    public ReportsController(TransactionService ts, UserService us) { super(us); this.transactionService = ts; }
    @GetMapping
    public String reports(Model model, @RequestParam(required=false) String type,
                          @RequestParam(required=false) String startDate,
                          @RequestParam(required=false) String endDate) {
        model.addAttribute("transactions", transactionService.search(null,type,null,startDate,endDate));
        model.addAttribute("activePage","reports");
        return "reports/index";
    }
}
