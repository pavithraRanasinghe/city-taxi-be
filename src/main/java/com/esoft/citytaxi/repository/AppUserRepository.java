package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
