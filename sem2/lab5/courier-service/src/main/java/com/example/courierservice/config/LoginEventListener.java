package com.example.courierservice.config;

import com.example.courierservice.model.LoginHistory;
import com.example.courierservice.repository.LoginHistoryRepository;
import com.example.courierservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LoginEventListener {

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    public LoginEventListener(UserRepository userRepository,
                              LoginHistoryRepository loginHistoryRepository) {
        this.userRepository = userRepository;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        System.out.println(">>> AUTH EVENT: " + username);

        userRepository.findByUsername(username).ifPresentOrElse(
                user -> {
                    String ip = "unknown";
                    try {
                        ServletRequestAttributes attrs =
                                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                        if (attrs != null) {
                            HttpServletRequest request = attrs.getRequest();
                            ip = request.getRemoteAddr();
                        }
                    } catch (Exception e) {
                        System.out.println(">>> Cannot get request: " + e.getMessage());
                    }

                    System.out.println(">>> FOUND user id=" + user.getId() + ", ip=" + ip);
                    loginHistoryRepository.save(new LoginHistory(user, ip));
                    System.out.println(">>> SAVED LoginHistory id=" + user.getId());
                },
                () -> System.out.println(">>> USER NOT FOUND: " + username)
        );
    }
}