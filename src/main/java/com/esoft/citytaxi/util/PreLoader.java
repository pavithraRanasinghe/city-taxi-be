package com.esoft.citytaxi.util;

import com.esoft.citytaxi.enums.UserType;
import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class PreLoader {

    @Bean
    CommandLineRunner initDatabase(AppUserRepository userRepository,
                                   PasswordEncoder passwordEncoder){
        return args -> {

            if (userRepository.findByUsername("admin@esoft.lk").isEmpty()) {
                final AppUser appUser = AppUser.builder()
                        .firstName("ADMIN")
                        .lastName("ESOFT")
                        .username("admin@esoft.lk")
                        .userType(UserType.ADMIN)
                        .password(passwordEncoder.encode("EsoftAdmin"))
                        .build();

                userRepository.save(appUser);
            }
            if (userRepository.findByUsername("operator@esoft.lk").isEmpty()) {
                final AppUser appUser = AppUser.builder()
                        .firstName("Admin")
                        .lastName("Operator")
                        .username("operator@esoft.lk")
                        .userType(UserType.OPERATOR)
                        .password(passwordEncoder.encode("Operator1234"))
                        .build();

                userRepository.save(appUser);
            }
        };
    }
}
