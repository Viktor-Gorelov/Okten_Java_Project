package com.okten.okten_project_java.controllers;

import com.okten.okten_project_java.dto.SendMailDTO;
import com.okten.okten_project_java.entities.User;
import com.okten.okten_project_java.entities.UserRole;
import com.okten.okten_project_java.repositories.UserRepository;
import com.okten.okten_project_java.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class MissingBrandController {
    private final UserRepository userRepository;
    private final MailService mailService;

    @PostMapping("/report-missing-brand")
    public ResponseEntity<Void> reportMissingBrand(@RequestBody String brand) {
        List<User> managers;
        managers = userRepository.findUserByUserRole(UserRole.MANAGER);
        String mailSubject = "Review Required for missing brand";
        String mailText = "We dont have this brand: " + brand + " and requires your review.";
        for (User manager : managers) {
            SendMailDTO mailDTO = SendMailDTO.builder()
                    .subject(mailSubject)
                    .text(mailText)
                    .recipient(manager.getEmail())
                    .build();
            mailService.sendMail(mailDTO);
        }
        return ResponseEntity.noContent().build();
    }
}
