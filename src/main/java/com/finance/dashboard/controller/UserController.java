package com.finance.dashboard.controller;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/admin/users")
public class UserController extends BaseController {
    public UserController(UserService us) { super(us); }
    @GetMapping public String list(Model m) { m.addAttribute("users",userService.getAllUsers()); m.addAttribute("activePage","users"); return "users/list"; }
    @GetMapping("/add") public String addForm(Model m) { m.addAttribute("user",new User()); m.addAttribute("activePage","users"); return "users/form"; }
    @PostMapping("/save") public String save(@ModelAttribute User u, RedirectAttributes ra) { userService.save(u); ra.addFlashAttribute("success","User saved."); return "redirect:/admin/users"; }
    @GetMapping("/edit/{id}") public String edit(@PathVariable Long id, Model m) { userService.getById(id).ifPresent(u->m.addAttribute("user",u)); m.addAttribute("activePage","users"); return "users/form"; }
    @GetMapping("/delete/{id}") public String delete(@PathVariable Long id, RedirectAttributes ra) { userService.delete(id); ra.addFlashAttribute("success","Deleted."); return "redirect:/admin/users"; }
}
