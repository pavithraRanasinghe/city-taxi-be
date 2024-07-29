package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.AppUserRequest;
import com.esoft.citytaxi.models.AppUser;
import com.esoft.citytaxi.services.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/app-user")
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Post mapping request for save new application user
     *
     * @param appUserRequest {@link AppUserRequest} the request body
     * @return {@link ResponseEntity<AppUser>} Http response with the newly created app user
     */
    @PostMapping
    public ResponseEntity<AppUser> saveNewUser(@RequestBody final AppUserRequest appUserRequest){
        AppUser appUser = appUserService.save(appUserRequest);
        return new ResponseEntity<>(appUser, HttpStatus.CREATED);
        // Can use both format
//        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }
}
