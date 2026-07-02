package com.example.rental_platform.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.ProfileResponse;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ApiResponse<ProfileResponse> getProfile() {

        // Get currently logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Find user in database
      
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Convert User Entity -> ProfileResponse DTO
        ProfileResponse profileResponse = ProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();

        // Return Standard API Response
        return ApiResponse.<ProfileResponse>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(profileResponse)
                .build();
    }
}