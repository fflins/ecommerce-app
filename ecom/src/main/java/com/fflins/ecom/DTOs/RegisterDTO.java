package com.fflins.ecom.DTOs;

import com.fflins.ecom.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank
        String login,
        @NotBlank
        @Size(min = 8)
        String password,
        @Email
        String email,
        Role role
) {}