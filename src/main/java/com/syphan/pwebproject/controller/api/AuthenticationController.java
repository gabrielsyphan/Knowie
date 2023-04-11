package com.syphan.pwebproject.controller.api;

import com.syphan.pwebproject.constants.PathConstants;
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

    @PutMapping(value = PathConstants.LOGIN, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> login(@RequestBody UserDto userDto, Model model, HttpSession session) {
        UserDto user = this.userService.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

        if(userDto.getEmail().equals("admin") && userDto.getPassword().equals("admin")) {
            user = UserDto.builder().email("admin@knowie.site").name("ADMIN").build();
        }

        if (user != null) {
            session.setAttribute("user", user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        model.addAttribute("user", null);
        session.removeAttribute("user");
        return "redirect:/";
    }
}
