package com.todolist.app.controllers;

import com.todolist.app.models.UserModel;
import com.todolist.app.security.AuthCredentials;
import com.todolist.app.security.TokenUtils;
import com.todolist.app.security.dto.JwtDto;
import com.todolist.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;

    @PostMapping("/create_user")
    public ResponseEntity<UserModel> saveUser(@RequestBody UserModel user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Validated AuthCredentials loginUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JwtDto("Error email no encontrado"));
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getEmail(),
                        loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TokenUtils.createToken(userService.findOneByEmail(loginUsuario.getEmail()), loginUsuario.getEmail());
        JwtDto jwtDto = new JwtDto(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }
}
