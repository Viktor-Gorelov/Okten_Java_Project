package com.okten.okten_project_java.controllers;

import com.okten.okten_project_java.dto.advertisement.AdvertisementCreateDTO;
import com.okten.okten_project_java.dto.advertisement.AdvertisementDTO;
import com.okten.okten_project_java.dto.advertisement.AdvertisementDetailsDTO;
import com.okten.okten_project_java.dto.advertisement.AdvertisementUpdateDTO;
import com.okten.okten_project_java.services.AdvertisementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @GetMapping()
    public ResponseEntity<List<AdvertisementDTO>> getActiveAdvertisements(){
        return ResponseEntity.ok(advertisementService.getActiveAdvertisements());
    }

    @PostMapping()
    public ResponseEntity<AdvertisementDTO> createAdvertisement(@RequestBody
                                                                    @Valid
                                                                    AdvertisementCreateDTO advertisementCreateDTO){
        AdvertisementDTO advertisementDTO  = AdvertisementDTO.builder()
                .brand(advertisementCreateDTO.getBrand())
                .model(advertisementCreateDTO.getModel())
                .description(advertisementCreateDTO.getDescription())
                .currency_type(advertisementCreateDTO.getCurrency_type())
                .price(advertisementCreateDTO.getPrice())
                .advertisement_status(advertisementCreateDTO.getAdvertisement_status())
                .region(advertisementCreateDTO.getRegion())
                .build();
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        advertisementDTO.setOwner(username);
        return ResponseEntity.ok(advertisementService.createAdvertisement(advertisementDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementDetailsDTO> getAdvertisementById(@PathVariable int id){
        return ResponseEntity.ok(advertisementService.getAdvertisementById(id));
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAllBrands() {
        return ResponseEntity.ok(advertisementService.getAllBrand());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementDTO> updateAdvertisementById(@PathVariable int id,
                                                                    @RequestBody AdvertisementUpdateDTO
                                                                            advertisementUpdateDTO){
        return ResponseEntity.ok(advertisementService.updateAdvertisement(id,advertisementUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisementById(@PathVariable int id){
        advertisementService.deleteAdvertisementById(id);
        return ResponseEntity.noContent().build();
    }
}
