package com.syphan.pwebproject.controller.api;

import com.syphan.pwebproject.constants.PathConstants;
import com.syphan.pwebproject.model.dto.AuthenticationDto;
import com.syphan.pwebproject.model.dto.TokenJwtDto;
import com.syphan.pwebproject.model.entity.UserEntity;
import com.syphan.pwebproject.service.authentication.TokenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("route", "login");
        return "generic/login";
    }

    @PostMapping(value = PathConstants.LOGIN, consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> authenticate(@RequestBody @Valid AuthenticationDto authenticationDto) {
        log.info("Authentication request: {}", authenticationDto.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        String tokenJwt = this.tokenService.generateToken((UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenJwtDto(tokenJwt));
    }
}
