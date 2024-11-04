package com.okten.okten_project_java.dto;

import com.okten.okten_project_java.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDTO {
    PaymentStatus status;
}
