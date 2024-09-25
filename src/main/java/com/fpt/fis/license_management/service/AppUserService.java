//package com.fpt.fis.license_management.service;
//
//import com.fpt.fis.license_management.repository.entity.AppUser;
//import com.fpt.fis.license_management.security.enums.Role;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class UserService {
//
//    private Map<String, AppUser> data;
//
//    @PostConstruct
//    public void init() {
//        data = new HashMap<>();
//
//        //username:passwowrd -> user:user
//        data.put("user", new AppUser("user", "31+l0BSLwH50RGNTlXO1/OFFCDj28WgBr3WCk8v2Q/Y=", true, Arrays.asList(Role.ROLE_USER)));
//
//        //username:passwowrd -> admin:admin
//        data.put("admin", new AppUser("admin", "+f4i1iURW6nUyGK60vfJaWYTWHUi4S88Ef2szj3N16U=", true, Arrays.asList(Role.ROLE_ADMIN)));
//    }
//
//    public Mono<AppUser> findByUsername(String username) {
//        return Mono.justOrEmpty(data.get(username));
//    }
//}

public interface AppUserService {
    findUser
}