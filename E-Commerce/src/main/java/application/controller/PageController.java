package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/admin")
    public String adminPage(Model model) {
        return "admin";
    }

    @GetMapping(value = "/employee")
    public String employeePage(Model model) {
        return "employee";
    }

    @GetMapping(value = "/customer")
    public String customerPage(Model model) {
        return "customer";
    }

    @GetMapping(value = "/cart")
    public String cartPage(Model model) {
        return "cart";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

}
