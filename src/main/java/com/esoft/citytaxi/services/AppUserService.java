package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.AppUserRequest;
import com.esoft.citytaxi.dto.request.AuthRequest;
import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.enums.UserType;
import com.esoft.citytaxi.exceptions.EntityExistsException;
import com.esoft.citytaxi.exceptions.EntityNotFoundException;
import com.esoft.citytaxi.exceptions.UnAuthorizedException;
import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final DriverService driverService;
    private final PassengerService passengerService;
    /**
     * Method for save application user
     *
     * @param userRequest {@link AppUserRequest} user detail request
     * @return {@link AppUser} Application user model
     */
    public AppUser signUp(final AppUserRequest userRequest){
        log.info("Save new user : {}", userRequest);

        AppUser registeredUser = this.findByUsername(userRequest.getUsername());
        if(Objects.nonNull(registeredUser))
            throw new EntityExistsException("User already registered ", userRequest.getUsername());
        final AppUser appUser = AppUser.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .userType(userRequest.getUserType())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();

        if(UserType.DRIVER.equals(appUser.getUserType())){
            driverService.saveDriver(BasicUserRequest.builder()
                            .firstName(appUser.getFirstName())
                            .lastName(appUser.getLastName())
                            .contact(userRequest.getContact()).build());
        }else if(UserType.PASSENGER.equals(appUser.getUserType())) {
            passengerService.savePassenger(BasicUserRequest.builder()
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .contact(userRequest.getContact()).build());
        }
        return appUserRepository.save(appUser);
    }

    public AppUser findByUsername(final String username){
        return appUserRepository.findByUsername(username)
                .orElse(null);
    }

    public AppUser authenticate(AuthRequest authRequest) {
        AppUser existUser = this.appUserRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()-> new EntityNotFoundException(authRequest.getUsername()));
        if (passwordEncoder.matches(authRequest.getPassword(), existUser.getPassword())){
            return existUser;
        }else {
            throw new UnAuthorizedException("Password doesn't match for user");
        }
    }
}
