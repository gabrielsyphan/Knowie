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

import java.util.Objects;

@Controller
@SessionAttributes("user")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PutMapping(value = PathConstants.LOGIN, consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto, HttpSession session) {
        UserDto user = this.userService.login(userDto, session);

        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping(value = PathConstants.RESET_PASSWORD, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> resetPassword(@RequestBody UserDto userDto, HttpSession session) {
        try {
            this.userService.resetPassword(userDto, session);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(value = PathConstants.FORGOT_PASSWORD, consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> forgotPassword(@RequestBody UserDto userDto) {
        try {
            UserDto user = this.userService.forgotPassword(userDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        model.addAttribute("user", null);
        session.removeAttribute("user");
        return "redirect:/";
    }
}
