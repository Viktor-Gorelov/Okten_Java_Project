package com.okten.okten_project_java.mapper;

import com.okten.okten_project_java.dto.advertisement.AdvertisementDTO;
import com.okten.okten_project_java.entities.Advertisement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementMapper {
    public AdvertisementDTO mapToDTO(Advertisement advertisement){
        return AdvertisementDTO.builder()
                .id(advertisement.getId())
                .brand(advertisement.getBrand())
                .model(advertisement.getModel())
                .description(advertisement.getDescription())
                .currency_type(advertisement.getCurrency_type())
                .price(advertisement.getPrice())
                .advertisement_status(advertisement.getAdvertisement_status())
                .edit_attempts(advertisement.getEdit_attempts())
                .region(advertisement.getRegion())
                .owner(advertisement.getOwner().getUsername())
                .build();
    }

    public Advertisement mapToAdvertisement(AdvertisementDTO advertisementDTO){
        Advertisement advertisement = new Advertisement();
        advertisement.setId(advertisementDTO.getId());
        advertisement.setBrand(advertisementDTO.getBrand());
        advertisement.setModel(advertisementDTO.getModel());
        advertisement.setDescription(advertisementDTO.getDescription());
        advertisement.setCurrency_type(advertisementDTO.getCurrency_type());
        advertisement.setPrice(advertisementDTO.getPrice());
        advertisement.setAdvertisement_status(advertisementDTO.getAdvertisement_status());
        advertisement.setRegion(advertisementDTO.getRegion());
        return advertisement;
    }
}
