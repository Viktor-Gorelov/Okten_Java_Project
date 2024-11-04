package com.okten.okten_project_java.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequestDTO {

    private String username;

    private String password;
}
