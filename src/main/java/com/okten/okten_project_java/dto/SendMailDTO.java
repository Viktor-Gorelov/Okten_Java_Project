package com.okten.okten_project_java.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendMailDTO {

    private String subject;
    private String text;
    private String recipient;

}
