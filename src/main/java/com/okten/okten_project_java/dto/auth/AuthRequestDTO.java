package com.okten.okten_project_java.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}