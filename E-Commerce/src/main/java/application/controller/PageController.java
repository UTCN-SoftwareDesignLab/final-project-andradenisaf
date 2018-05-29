package application.controller;

import application.entity.Type;
import application.service.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    ITypeService typeService;

    @Autowired
    public PageController(ITypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping(value = "/admin")
    public String adminPage(Model model) {
        return "admin";
    }

    @GetMapping(value = "/employee")
    public String employeePage(Model model) {
        List<Type> types = typeService.findAll();
        model.addAttribute("types", types);
        return "employee";
    }

    @GetMapping(value = "/customer")
    public String customerPage(Model model) {
        return "index";
    }

    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

}
