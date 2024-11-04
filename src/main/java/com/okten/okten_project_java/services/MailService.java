package com.okten.okten_project_java.services;

import com.okten.okten_project_java.dto.SendMailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailSender mailSender;

    @Value("${mail.sender.username}")
    private String mailSenderUserName;

    public void sendMail(SendMailDTO mailDTO){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSenderUserName);
        mailMessage.setTo(mailDTO.getRecipient());
        mailMessage.setSubject(mailDTO.getSubject());
        mailMessage.setText(mailDTO.getText());
        mailSender.send(mailMessage);
    }
}
