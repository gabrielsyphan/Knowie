package com.syphan.pwebproject.controller.api;

import com.syphan.pwebproject.constants.PathConstants;
import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.service.user.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = PathConstants.USERS, consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto, UriComponentsBuilder uriComponentsBuilder) throws Exception {
        log.info("UserController - createUser: Create user: {}", userDto);
        UserDto createdUser = this.userService.create(userDto);
        URI uri = uriComponentsBuilder.path("users/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @GetMapping("/users")
    public String users(Model model) {
        log.info("UserController - getAllUsers: Get all users");
        List<UserDto> users = this.userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("route", "users");
        return "users/all";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("route", "users");
        return "users/new";
    }
}
