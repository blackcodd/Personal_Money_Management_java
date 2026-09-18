package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class NavigationController {
    @GetMapping("dashboard")
    public  String dashboard(@RequestParam("user_id") String userId,Model model){
        model.addAttribute("user_id",userId);
        return  "dashboard";
    }

    @GetMapping("income-expense")
    public String incomeExpense(@RequestParam("user_id") String userId, Model model) {
        model.addAttribute("user_id", userId);
        return "income-expense";
    }

    @GetMapping("budget")
    public String budgetPlanner(@RequestParam("user_id") String userId, Model model) {
        model.addAttribute("user_id", userId);
        return "budget";
    }

    @GetMapping("goal")
    public String financialGoals(@RequestParam("user_id") String userId, Model model) {
        model.addAttribute("user_id", userId);
        return "goal";
    }

    @GetMapping("report")
    public String reports(@RequestParam("user_id") String userId, Model model) {
        model.addAttribute("user_id", userId);
        return "report";
    }

    @GetMapping("settings")
    public String settings(@RequestParam("user_id") String userId, Model model) {
        model.addAttribute("user_id", userId);
        return "settings";
    }
}
