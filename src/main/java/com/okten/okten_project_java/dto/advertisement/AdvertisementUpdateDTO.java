package com.okten.okten_project_java.dto.advertisement;

import com.okten.okten_project_java.enums.AdvertisementStatus;
import com.okten.okten_project_java.enums.PriceCurrencyType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertisementUpdateDTO {
    @NotBlank(message = "Brand cannot be empty")
    private String brand;
    @NotBlank(message = "Model cannot be empty")
    private String model;
    @NotBlank(message = "Description cannot be empty")
    @Size(min = 10, max = 200, message = "Description must be at least 10 characters long and max 200")
    private String description;
    private PriceCurrencyType currency_type;
    @Min(value = 0, message = "Price must be at least 0")
    @Max(value = 1_000_000, message = "Price must be at most 1,000,000")
    private int price;
}
