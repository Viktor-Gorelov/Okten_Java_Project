package com.okten.okten_project_java.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyRateDTO {
    private String ccy;
    private String baseCcy;
    private double buy;
    private double sale;
}
