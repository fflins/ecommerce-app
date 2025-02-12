package com.fflins.ecom.DTOs;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank
        String login,
        @NotBlank
        String password
) {}

