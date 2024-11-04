package com.okten.okten_project_java.repositories;

import com.okten.okten_project_java.entities.Advertisement;
import com.okten.okten_project_java.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    @Query("SELECT AVG(a.price) FROM Advertisement a WHERE a.region = :region AND a.brand = :brand AND a.model = :model")
    Double findAveragePriceByRegion
            (@Param("region") String region, @Param("brand") String brand, @Param("model") String model);

    @Query("SELECT AVG(a.price) FROM Advertisement a WHERE a.brand = :brand AND a.model = :model")
    Double findAveragePriceInUkraine(@Param("brand") String brand, @Param("model") String model);

    List<Advertisement> findByOwner(User owner);
    @Query("SELECT DISTINCT a.brand FROM Advertisement a")
    List<String> findAllUniqueBrands();
}
