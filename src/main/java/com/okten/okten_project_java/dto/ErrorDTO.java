package com.okten.okten_project_java.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDTO {
    private String details;
    private LocalDateTime timestamp;
}
