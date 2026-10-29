package com.example.courierservice.controller;

import com.example.courierservice.model.User;
import com.example.courierservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {

        // Простая валидация
        if (username.isBlank() || password.isBlank() || fullName.isBlank()) {
            model.addAttribute("error", "Все поля обязательны");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register";
        }
        if (email != null && !email.isBlank() && userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            return "register";
        }

        User newUser = new User(username, fullName, email,
                encoder.encode(password), "USER");
        userRepository.save(newUser);

        return "redirect:/login?registered";
    }
}