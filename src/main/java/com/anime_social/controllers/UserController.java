package com.anime_social.controllers;

import com.anime_social.dto.request.UpdateUserRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/user")
@RestControllerAdvice
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping("/get-all")
    public AppResponse getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get/{id}")
    public AppResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/{id}")
    public AppResponse updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
