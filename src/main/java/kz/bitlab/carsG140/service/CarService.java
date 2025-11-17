package kz.bitlab.carsG140.service;

import kz.bitlab.carsG140.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    Page<Car> findAll(String name, Integer minYear, Integer maxPrice, Long countryId, Long categoryId, Pageable pageable);

    void addCar(String name, String model, Integer year, Integer price, Long countryId);

    Car findCarById(Long id);

    void updateCar(Long id, String name, String model, Integer year, Integer price);

    void deleteCarById(Long id);

    void assignCategory(Long categoryId, Long carId);

    void unassignCategory(Long categoryId, Long carId);
}
