package com.syphan.pwebproject.controller.web;

import com.syphan.pwebproject.model.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/")
public class GenericPagesWebController {

    @GetMapping
    public String home(Model model) {
        model.addAttribute("route", "home");
        return "generic/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("route", "login");
        return "generic/login";
    }
}
