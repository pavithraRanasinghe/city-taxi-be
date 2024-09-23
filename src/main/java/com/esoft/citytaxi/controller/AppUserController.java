package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.AppUserRequest;
import com.esoft.citytaxi.dto.request.AuthRequest;
import com.esoft.citytaxi.dto.response.JwtResponse;
import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.security.JwtTokenUtil;
import com.esoft.citytaxi.security.JwtUserDetailsService;
import com.esoft.citytaxi.services.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/app-user")
@Slf4j
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final JwtUserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Post mapping request for save new application user
     *
     * @param appUserRequest {@link AppUserRequest} the request body
     * @return {@link ResponseEntity<AppUser>} Http response with the newly created app user
     */
    @PostMapping
    public ResponseEntity<AppUser> register(@RequestBody final AppUserRequest appUserRequest){
        AppUser appUser = appUserService.signUp(appUserRequest);
        return new ResponseEntity<>(appUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthRequest authRequest) {
        AppUser existingUser = appUserService.authenticate(authRequest);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(existingUser.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(existingUser, token));
    }

    @PutMapping("/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable final String email){
        appUserService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}/verify-otp")
    public ResponseEntity<?> verifyOtp(@PathVariable final String email,
                                       @RequestParam("otp") final int otp){
        appUserService.verifyOtp(email, otp);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{email}/update-password")
    public ResponseEntity<?> verifyOtp(@PathVariable final String email,
                                       @RequestParam("password") final String password){
        appUserService.updatePassword(email, password);
        return ResponseEntity.ok().build();
    }
}
