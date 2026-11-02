package com.example.courierservice.config;

import com.example.courierservice.model.User;
import com.example.courierservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(new User("user", "Иван Клиентов",
                    "user@courier.local", encoder.encode("password"), "USER"));
            userRepository.save(new User("courier", "Пётр Курьеров",
                    "courier@courier.local", encoder.encode("password"), "COURIER"));
            userRepository.save(new User("moderator", "Ольга Диспетчерова",
                    "moderator@courier.local", encoder.encode("password"), "MODERATOR"));
        }
    }
}