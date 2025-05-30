package com.anime_social.controllers;

import com.anime_social.dto.request.UpdateUser;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.interfaces.UserService;

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
@Slf4j
public class UserController {
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-paging")
    public AppResponse getUsersPaging(@RequestParam int page, @RequestParam int size) {
        return userService.getUsersPaging(page, size);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get/{id}")
    public AppResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/{id}")
    public AppResponse updateUser(@PathVariable String id, @RequestBody UpdateUser userRequest) {
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

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/warning/{id}")
    public AppResponse warningUser(@PathVariable String id) {
        return userService.warningUser(id);
    }

    @GetMapping("/get-top")
    public AppResponse getTopUsers() {
        return userService.getTopUsers();
    }
}
