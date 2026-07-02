
package com.example.rental_platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.ProfileResponse;
import com.example.rental_platform.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<ProfileResponse> getProfile() {
        return userService.getProfile();
    }
}