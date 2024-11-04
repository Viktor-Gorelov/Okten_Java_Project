package com.okten.okten_project_java.controllers;

import com.okten.okten_project_java.dto.auth.SignUpRequestDTO;
import com.okten.okten_project_java.dto.auth.SignUpResponseDTO;
import com.okten.okten_project_java.entities.UserRole;
import com.okten.okten_project_java.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers")
public class ManagerController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDto) {
        SignUpResponseDTO signUpResponseDto = userService.createAuthorizeUser(signUpRequestDto, UserRole.MANAGER);
        return ResponseEntity.ok(signUpResponseDto);
    }
}
