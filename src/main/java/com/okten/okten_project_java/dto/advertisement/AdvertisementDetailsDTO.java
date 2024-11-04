package com.okten.okten_project_java.dto.advertisement;

import com.okten.okten_project_java.dto.CurrencyInfoDTO;
import com.okten.okten_project_java.enums.AdvertisementStatus;
import com.okten.okten_project_java.enums.PriceCurrencyType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdvertisementDetailsDTO {
    private Long id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String description;
    private PriceCurrencyType currency_type;
    @Min(value = 0, message = "price min 0")
    @Max(value = 1_000_000, message = "price max 1000000")
    private int price;
    private AdvertisementStatus advertisement_status;
    private int edit_attempts;
    private String owner;
    private List<CurrencyInfoDTO> additionalCurrencies;
    @NotBlank
    private String region;
    private int viewCount;
    private int dailyViews;
    private int weeklyViews;
    private int monthlyViews;
    private double averageRegionalPrice;
    private double averageUkrainePrice;
}
