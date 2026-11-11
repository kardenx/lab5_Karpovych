package ua.com.reactive.lab_karpovych.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab_karpovych.service.UserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registrationForm() {
        return "registration";
    }

    @PostMapping("/registration")
    public Mono<String> register(ServerWebExchange exchange, Model model) {
        return exchange.getFormData().flatMap(form -> {
            String username = form.getFirst("username");
            String password = form.getFirst("password");

            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                model.addAttribute("error", "Заповніть логін і пароль");
                return Mono.just("registration");
            }

            return userService.register(username.trim(), password)
                    .map(created -> {
                        if (created) {
                            return "redirect:/login";
                        }
                        model.addAttribute("error", "Користувач з таким логіном уже існує");
                        return "registration";
                    });
        });
    }
}