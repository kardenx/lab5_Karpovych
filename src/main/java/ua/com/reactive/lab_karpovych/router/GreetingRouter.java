package ua.com.reactive.lab_karpovych.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import ua.com.reactive.lab_karpovych.handler.GreetingHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {

        RequestPredicate accept = accept(MediaType.APPLICATION_JSON);

        return RouterFunctions
                .route(RequestPredicates.GET("/"), greetingHandler::hello)
                .andRoute(RequestPredicates.GET("/users").and(accept), greetingHandler::users)
                .andRoute(RequestPredicates.GET("/admin"), greetingHandler::admin);
    }
}