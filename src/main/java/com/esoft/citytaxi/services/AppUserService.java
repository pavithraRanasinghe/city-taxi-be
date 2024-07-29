package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.AppUserRequest;
import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Method for save application user
     *
     * @param userRequest {@link AppUserRequest} user detail request
     * @return {@link AppUser} Application user model
     */
    public AppUser save(final AppUserRequest userRequest){
        log.info("Save new user : {}", userRequest);
        final AppUser appUser = AppUser.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();

        return appUserRepository.save(appUser);
    }
}
