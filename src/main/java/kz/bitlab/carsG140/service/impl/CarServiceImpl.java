package kz.bitlab.carsG140.service.impl;

import kz.bitlab.carsG140.entity.Car;
import kz.bitlab.carsG140.entity.Category;
import kz.bitlab.carsG140.entity.Country;
import kz.bitlab.carsG140.repository.CarRepository;
import kz.bitlab.carsG140.repository.CategoryRepository;
import kz.bitlab.carsG140.repository.CountryRepository;
import kz.bitlab.carsG140.service.CarService;
import kz.bitlab.carsG140.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CountryRepository countryRepository;
    private final CarRepository carRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Car> findAll(String name, Integer minYear, Integer maxPrice, Long countryId, Long categoryId, Pageable pageable) {
        Specification<Car> specification = CarSpecification.getCarSpecification(name, minYear, maxPrice, countryId, categoryId);
        Page<Car> carsPage = carRepository.findAll(specification, pageable);
        return carsPage;
    }

    @Override
    public void addCar(String name, String model, Integer year, Integer price, Long countryId) {
        Country country = countryRepository.findById(countryId).orElseThrow(() ->
                new RuntimeException("Country not exist"));
        Car car = Car.builder()
                .name(name)
                .model(model)
                .year(year)
                .price(price)
                .country(country)
                .build();
        carRepository.save(car);
    }

    @Override
    public Car findCarById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car is not found"));
    }

    @Override
    public void updateCar(Long id, String name, String model, Integer year, Integer price) {
        Car car = findCarById(id);
        car.setName(name);
        car.setYear(year);
        car.setPrice(price);
        car.setModel(model);
        carRepository.save(car);
    }

    @Override
    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public void assignCategory(Long categoryId, Long carId) {
        Car car = findCarById(carId);
        Category category = findCategory(categoryId);
        car.getCategories().add(category);
        carRepository.save(car);
    }

    @Override
    public void unassignCategory(Long categoryId, Long carId) {
        Car car = findCarById(carId);
        Category category = findCategory(categoryId);

        car.getCategories().remove(category);
        carRepository.save(car);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category is not found"));
    }
}
