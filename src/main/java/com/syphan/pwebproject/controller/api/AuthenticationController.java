package com.syphan.pwebproject.controller.api;

import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("user")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserDto userDto, Model model) {
        UserDto user = this.userService.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        if (user != null) {
            model.addAttribute("user", user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/authenticated")
    public ResponseEntity<UserDto> getUser(@ModelAttribute("user") UserDto user) {
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        model.addAttribute("user", null);
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/make-login-develop-test")
    public String makeLoginDevelopTest(Model model) {
        model.addAttribute("user", new UserDto());
        return "redirect:/";
    }
}
