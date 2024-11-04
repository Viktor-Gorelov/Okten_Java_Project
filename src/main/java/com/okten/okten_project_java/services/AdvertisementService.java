package com.okten.okten_project_java.services;

import com.modernmt.text.profanity.ProfanityFilter;
import com.modernmt.text.profanity.dictionary.Profanity;
import com.okten.okten_project_java.dto.CurrencyInfoDTO;
import com.okten.okten_project_java.dto.SendMailDTO;
import com.okten.okten_project_java.dto.advertisement.AdvertisementDTO;
import com.okten.okten_project_java.dto.advertisement.AdvertisementDetailsDTO;
import com.okten.okten_project_java.dto.advertisement.AdvertisementUpdateDTO;
import com.okten.okten_project_java.entities.Advertisement;
import com.okten.okten_project_java.entities.User;
import com.okten.okten_project_java.entities.UserRole;
import com.okten.okten_project_java.enums.AdvertisementStatus;
import com.okten.okten_project_java.enums.PriceCurrencyType;
import com.okten.okten_project_java.enums.UserStatus;
import com.okten.okten_project_java.mapper.AdvertisementMapper;
import com.okten.okten_project_java.repositories.AdvertisementRepository;
import com.okten.okten_project_java.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final UserRepository userRepository;
    private final CurrencyService currencyService;
    private final MailService mailService;
    private final ProfanityFilter profanityFilter = new ProfanityFilter();

    public List<AdvertisementDTO> getActiveAdvertisements(){
        return advertisementRepository
                .findAll()
                .stream()
                .filter(a -> a.getAdvertisement_status() == AdvertisementStatus.ACTIVE)
                .map(advertisementMapper::mapToDTO)
                .toList();
    }

    public AdvertisementDTO createAdvertisement(AdvertisementDTO advertisementDTO){
        User owner = userRepository.findByUsername(advertisementDTO.getOwner());
        List<Advertisement> advertisements = advertisementRepository.findByOwner(owner);
        if (owner.getUserStatus() == UserStatus.STANDARD && !advertisements.isEmpty()) {
            throw new SecurityException("User with base account may only have one advertisement.");
        }
        if(owner.getIsBanned()){
            throw new SecurityException("The user is banned and therefore cannot create an ad");
        }

        Advertisement advertisement = advertisementMapper.mapToAdvertisement(advertisementDTO);
        advertisement.setOwner(userRepository.findByUsername(advertisementDTO.getOwner()));

        final Profanity profanity = profanityFilter.find("en", advertisementDTO.getDescription());
        if(profanity!= null){
            advertisement.setAdvertisement_status(AdvertisementStatus.INACTIVE);
        } else {
            advertisement.setAdvertisement_status(AdvertisementStatus.ACTIVE);
        }
        advertisement.setEdit_attempts(3);
        Advertisement save = advertisementRepository.save(advertisement);
        return advertisementMapper.mapToDTO(save);
    }

    public AdvertisementDetailsDTO getAdvertisementById(int id){
        Advertisement advertisement = advertisementRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("advertisement with this id isn't present"));
        AdvertisementDetailsDTO advertisementDetailsDTO = AdvertisementDetailsDTO.builder()
                .id(advertisement.getId())
                .brand(advertisement.getBrand())
                .model(advertisement.getModel())
                .description(advertisement.getDescription())
                .currency_type(advertisement.getCurrency_type())
                .price(advertisement.getPrice())
                .advertisement_status(advertisement.getAdvertisement_status())
                .owner(advertisement.getOwner().getUsername())
                .region(advertisement.getRegion())
                .build();
        advertisementDetailsDTO.setAdditionalCurrencies(getAdditionalCurrencies(advertisement.getPrice(), advertisement.getCurrency_type()));
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User currentUser = userRepository.findByUsername(currentUsername);
        if(currentUser.getUserStatus() == UserStatus.VIP){
            advertisementDetailsDTO.setViewCount(advertisement.getViewCount());
            advertisementDetailsDTO.setDailyViews(advertisement.getDailyViews());
            advertisementDetailsDTO.setWeeklyViews(advertisement.getWeeklyViews());
            advertisementDetailsDTO.setMonthlyViews(advertisement.getMonthlyViews());
            Double averagePriceByRegion = advertisementRepository.findAveragePriceByRegion(advertisement.getRegion(),
                    advertisement.getBrand(), advertisement.getModel());
            if(averagePriceByRegion == null){
                averagePriceByRegion = 0.0;
            }
            Double averageUkrainePrice = advertisementRepository.findAveragePriceInUkraine(
                    advertisement.getBrand(), advertisement.getModel());
            if(averageUkrainePrice == null){
                averageUkrainePrice = 0.0;
            }
            advertisementDetailsDTO.setAverageRegionalPrice(averagePriceByRegion);
            advertisementDetailsDTO.setAverageUkrainePrice(averageUkrainePrice);
        }
        advertisement.setViewCount(advertisement.getViewCount() + 1);
        advertisement.setDailyViews(advertisement.getDailyViews() + 1);
        advertisement.setWeeklyViews(advertisement.getWeeklyViews() + 1);
        advertisement.setMonthlyViews(advertisement.getMonthlyViews() + 1);
        advertisementRepository.save(advertisement);
        return advertisementDetailsDTO;
    }

    private List<CurrencyInfoDTO> getAdditionalCurrencies(int amount, PriceCurrencyType currentType) {
        List<CurrencyInfoDTO> additionalCurrencies = new ArrayList<>();
        for (PriceCurrencyType type : PriceCurrencyType.values()) {
            if (type == currentType) {
                continue;
            }
            additionalCurrencies.add(CurrencyInfoDTO.builder()
                    .type(type)
                    .value(currencyService.convert(amount, currentType.toString(), type.toString()))
                    .rate(currencyService.convert(1, type.toString(), currentType.toString()))
                    .build());
        }
        return additionalCurrencies;
    }

    public List<String> getAllBrand(){
        return advertisementRepository.findAllUniqueBrands();
    }

    public AdvertisementDTO updateAdvertisement(int id, AdvertisementUpdateDTO advertisementUpdateDTO){
        Advertisement advertisement = advertisementRepository.findById(id)
                .filter(advertisement1 ->{
                    String advertisementOwner = advertisement1.getOwner().getUsername();
                    String currentUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
                    return Objects.equals(advertisementOwner, currentUserName);
                })
                .orElseThrow(() -> new NoSuchElementException("advertisement with this id isn't present"));
        if (advertisement.getEdit_attempts() == 0) {
            throw new IllegalStateException("This advertisement cannot be edited anymore, edit attempts exceeded.");
        }
        advertisement.setBrand(advertisementUpdateDTO.getBrand());
        advertisement.setModel(advertisementUpdateDTO.getModel());
        advertisement.setDescription(advertisementUpdateDTO.getDescription());
        advertisement.setCurrency_type(advertisementUpdateDTO.getCurrency_type());
        advertisement.setPrice(advertisementUpdateDTO.getPrice());
        final Profanity profanity = profanityFilter.find("en", advertisementUpdateDTO.getDescription());

        if (profanity!= null) {
            if (advertisement.getEdit_attempts() > 1) {
                advertisement.setEdit_attempts(advertisement.getEdit_attempts() - 1);
                advertisement.setAdvertisement_status(AdvertisementStatus.INACTIVE);
            }
            else {
                advertisement.setEdit_attempts(0);
                advertisement.setAdvertisement_status(AdvertisementStatus.INACTIVE);
                List<User> managers = userRepository.findUserByUserRole(UserRole.MANAGER);
                String mailSubject = "Review Required for Inactive Advertisement";
                String mailText = "Advertisement with ID " + id + " has reached 0 edit attempts and requires your review.";
                for (User manager : managers) {
                    SendMailDTO mailDTO = SendMailDTO.builder()
                            .subject(mailSubject)
                            .text(mailText)
                            .recipient(manager.getEmail())
                            .build();
                    mailService.sendMail(mailDTO);
                }
            }
        } else {
            advertisement.setEdit_attempts(3);
            advertisement.setAdvertisement_status(AdvertisementStatus.ACTIVE);
        }
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);
        return advertisementMapper.mapToDTO(savedAdvertisement);
    }

    public void deleteAdvertisementById(int id){
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Advertisement with this id isn't present"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User currentUser = userRepository.findByUsername(currentUsername);

        if (advertisement.getAdvertisement_status() == AdvertisementStatus.ACTIVE &&
                currentUser.getUserRole().equals(UserRole.MANAGER)) {
            throw new SecurityException("Managers cannot delete active advertisement.");
        }
        advertisementRepository.deleteById(id);
    }
}
