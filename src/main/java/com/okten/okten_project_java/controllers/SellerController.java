package com.okten.okten_project_java.controllers;

import com.okten.okten_project_java.dto.PaymentDTO;
import com.okten.okten_project_java.entities.User;
import com.okten.okten_project_java.enums.PaymentStatus;
import com.okten.okten_project_java.enums.UserStatus;
import com.okten.okten_project_java.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final UserRepository userRepository;

    @PutMapping("/upgrade-to-vip")
    public ResponseEntity<Void> upgradeToVip(@RequestBody @Valid PaymentDTO paymentDTO) {
        if(paymentDTO.getStatus() == PaymentStatus.PASS){
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            User currentUser = userRepository.findByUsername(currentUsername);
            currentUser.setUserStatus(UserStatus.VIP);
            userRepository.save(currentUser);
        } else {
            throw new SecurityException("The user has not paid");
        }
        return ResponseEntity.noContent().build();
    }
}
