package com.fpt.fis.license_management.repository;

import com.fpt.fis.license_management.repository.entity.AppUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AppUserRepository extends ReactiveMongoRepository<AppUser, String> {
}
