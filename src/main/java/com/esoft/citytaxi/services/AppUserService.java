package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.EmailMapper;
import com.esoft.citytaxi.dto.request.AppUserRequest;
import com.esoft.citytaxi.dto.request.AuthRequest;
import com.esoft.citytaxi.dto.request.BasicUserRequest;
import com.esoft.citytaxi.enums.UserType;
import com.esoft.citytaxi.exceptions.EntityExistsException;
import com.esoft.citytaxi.exceptions.EntityNotFoundException;
import com.esoft.citytaxi.exceptions.ExpectationFailedException;
import com.esoft.citytaxi.exceptions.UnAuthorizedException;
import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.models.Driver;
import com.esoft.citytaxi.models.Passenger;
import com.esoft.citytaxi.repository.AppUserRepository;
import com.esoft.citytaxi.util.PasswordUtil;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final DriverService driverService;
    private final PassengerService passengerService;
    private final MailService mailService;
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
            Driver driver = driverService.saveDriver(BasicUserRequest.builder()
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .contact(userRequest.getContact()).build());
            appUser.setDriver(driver);
        }else if(UserType.PASSENGER.equals(appUser.getUserType())) {
            Passenger passenger = passengerService.savePassenger(BasicUserRequest.builder()
                    .firstName(appUser.getFirstName())
                    .lastName(appUser.getLastName())
                    .contact(userRequest.getContact()).build());
            appUser.setPassenger(passenger);
        }
        AppUser save = appUserRepository.save(appUser);
        emailUserPassword(save, userRequest.getPassword());
        return save;
    }

    public AppUser findByUsername(final String username){
        return appUserRepository.findByUsername(username)
                .orElse(null);
    }

    public AppUser authenticate(AuthRequest authRequest) {
        AppUser existUser = this.appUserRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()-> new EntityNotFoundException(authRequest.getUsername()));
        emailUserPassword(existUser, "TEST PASSWORD");
        if (passwordEncoder.matches(authRequest.getPassword(), existUser.getPassword())){
            return existUser;
        }else {
            throw new UnAuthorizedException("Password doesn't match for user");
        }
    }

    public void forgotPassword(final String email){

        AppUser appUser = appUserRepository.findByUsername(email)
                .orElseThrow(() -> new ExpectationFailedException("Email not found"));

        Random random = new Random();
        int otp = random.nextInt(900000) + 100000;

        appUser.setPasswordOtp(otp);

        appUserRepository.save(appUser);

        log.info("Initiating Email for forgot password. Sending to {}", appUser.getUsername());
        EmailMapper emailMapper = new EmailMapper();
        emailMapper.setTo(appUser.getUsername());
        emailMapper.setTemplateName("forgotPassword.ftl");
        emailMapper.setSubject("Forgot password - OTP");
        Map<String, Object> model = new HashMap<>();
        model.put("name", appUser.getFirstName() + " " + appUser.getLastName());
        model.put("otp", otp);

        emailMapper.setModel(model);
        try {
            mailService.sendEmail(emailMapper);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send the forgot password email, Email Address : {}", appUser.getUsername());
            log.error(e.toString());
            throw new ExpectationFailedException("Failed to send the Email");
        }
    }

    public void verifyOtp(final String email, final int otp){
        AppUser appUser = appUserRepository.findByUsername(email)
                .orElseThrow(() -> new ExpectationFailedException("Email not found"));

        if(appUser.getPasswordOtp() != otp){
            throw new UnAuthorizedException("OTP doesn't match for user");
        }
        appUser.setPasswordOtp(0);
        appUserRepository.save(appUser);
    }

    public void updatePassword(final String email, final String password){
        AppUser appUser = appUserRepository.findByUsername(email)
                .orElseThrow(() -> new ExpectationFailedException("Email not found"));


        appUser.setPassword(passwordEncoder.encode(password));

        appUserRepository.save(appUser);
    }

    private void emailUserPassword(AppUser appUser, String password) {

        log.info("Initiating Email for user password. Sending to {}", appUser.getUsername());
        EmailMapper emailMapper = new EmailMapper();
        emailMapper.setTo(appUser.getUsername());
        emailMapper.setTemplateName("password.ftl");
        emailMapper.setSubject("User password");
        Map<String, Object> model = new HashMap<>();
        model.put("name", appUser.getFirstName() + " " + appUser.getLastName());
        model.put("userName", appUser.getUsername());
        model.put("password", password);
        model.put("type", appUser.getUserType().toString());

        emailMapper.setModel(model);
        try {
            mailService.sendEmail(emailMapper);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send the Email, Email Address : {}", appUser.getUsername());
            log.error(e.toString());
            throw new ExpectationFailedException("Failed to send the Email");
        }
    }
}
