package com.okten.okten_project_java.dto;

import com.okten.okten_project_java.enums.PriceCurrencyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyInfoDTO {
    private PriceCurrencyType type;
    private double value;
    private double rate;
}
