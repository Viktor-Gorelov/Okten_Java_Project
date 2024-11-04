package com.okten.okten_project_java.entities;


import com.okten.okten_project_java.enums.AdvertisementStatus;
import com.okten.okten_project_java.enums.PriceCurrencyType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    private String description;
    @Enumerated(EnumType.STRING)
    private PriceCurrencyType currency_type;
    private int price;
    @Enumerated(EnumType.STRING)
    private AdvertisementStatus advertisement_status;
    private int edit_attempts;
    private String region;
    private int viewCount;
    private int dailyViews;
    private int weeklyViews;
    private int monthlyViews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;
}
