package com.finance.dashboard.controller;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/bills")
public class BillsController extends BaseController {
    public BillsController(UserService us) { super(us); }
    @GetMapping
    public String bills(Model model) { model.addAttribute("activePage","bills"); return "bills/index"; }
}
