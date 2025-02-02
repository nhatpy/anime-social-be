package com.anime_social.controller;

import com.anime_social.dto.request.UpdateUserRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
@RestControllerAdvice
@Slf4j
public class UserController {
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public AppResponse getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get/{id}")
    public AppResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/update/{id}")
    public AppResponse updateUser(@PathVariable String id, @RequestBody UpdateUserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public AppResponse deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/current")
    public AppResponse getCurrentUser() {
        return userService.currentUser();
    }
}
