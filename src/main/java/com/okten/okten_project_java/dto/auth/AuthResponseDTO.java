package com.okten.okten_project_java.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String accessToken;

    private String refreshToken;
}
