package com.okten.okten_project_java.job;

import com.okten.okten_project_java.entities.Advertisement;
import com.okten.okten_project_java.repositories.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateAdvertisementViewCount {
    private final AdvertisementRepository advertisementRepository;

    @Scheduled(cron = "@daily")
    public void updateDailyViewCount(){
        List<Advertisement> advertisements = advertisementRepository.findAll();
        for(Advertisement advertisement: advertisements){
            advertisement.setDailyViews(0);
            advertisementRepository.save(advertisement);
        }
    }

    @Scheduled(cron = "@weekly")
    public void updateWeeklyViewCount(){
        List<Advertisement> advertisements = advertisementRepository.findAll();
        for(Advertisement advertisement: advertisements){
            advertisement.setWeeklyViews(0);
            advertisementRepository.save(advertisement);
        }
    }

    @Scheduled(cron = "@monthly")
    public void updateMonthlyViewCount(){
        List<Advertisement> advertisements = advertisementRepository.findAll();
        for(Advertisement advertisement: advertisements){
            advertisement.setMonthlyViews(0);
            advertisementRepository.save(advertisement);
        }
    }

}
