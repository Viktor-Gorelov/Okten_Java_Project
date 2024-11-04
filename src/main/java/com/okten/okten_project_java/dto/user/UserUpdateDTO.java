package com.okten.okten_project_java.dto.user;

import com.okten.okten_project_java.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDTO {
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 16, message = "Password must be at least 6 characters long and max 16")
    private String password;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
}
