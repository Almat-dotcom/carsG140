package kz.bitlab.carsG140.controller;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import kz.bitlab.carsG140.entity.Car;
import kz.bitlab.carsG140.entity.Category;
import kz.bitlab.carsG140.entity.Country;
import kz.bitlab.carsG140.repository.CarRepository;
import kz.bitlab.carsG140.repository.CategoryRepository;
import kz.bitlab.carsG140.repository.CountryRepository;
import kz.bitlab.carsG140.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarRepository carRepository;
    private final CountryRepository countryRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public String all(@RequestParam(name = "car_name", required = false) String name,
                      @RequestParam(name = "min_year", required = false) Integer minYear,
                      @RequestParam(name = "max_price", required = false) Integer maxPrice,
                      @RequestParam(name = "category_id", required = false) Long categoryId,
                      @RequestParam(name = "countryId", required = false) Long countryId,
                      Model model
    ) {
//        List<Car> cars = carRepository.findAll();
//        cars = cars.stream()
//                .filter(e -> e.getYear() > 2010)
//                .toList();

        //Specification
        //Predicate=

//        if (name != null && minYear != null && maxPrice != null) {
//            // Если в URL-запросе присутствует параметр year, передаем представлению список машин, у которых год производства соответствует значению параметра year
//            cars = carRepository.findAllByNameAndYearGreaterThanAndPriceLessThan(name, minYear, maxPrice);
//        } else if (name != null && minYear != null) {
//            cars = carRepository.findAllByNameAndYearGreaterThan(name, minYear);
//        } else if (name != null && maxPrice != null) {
//            cars = carRepository.findAllByNameAndPriceLessThan(name, maxPrice);
//        } else {
//            // Если в URL-запросе отсутствует параметр year, передаем представлению список всех машин
//            cars = carRepository.findAll();
//        }
        List<Car> cars = carRepository.findAll(CarSpecification.getCarSpecification(name, minYear, maxPrice, countryId, categoryId));
//        List<Car> cars = carRepository.findAllBySomeParam(name, minYear, maxPrice);

        model.addAttribute("cars", cars);
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "cars";
    }

    @GetMapping("/addcar")
    public String addCar(Model model) {
        model.addAttribute("countries", countryRepository.findAll());
        return "add-car";
    }

    @PostMapping("/addcar")
    public String addCarPost(@RequestParam(name = "car_name") String name,
                             @RequestParam(name = "car_model") String model,
                             @RequestParam(name = "car_year") Integer year,
                             @RequestParam(name = "car_price") Integer price,
                             @RequestParam(name = "country_id") Long countryId) {
        Country country = countryRepository.findById(countryId).orElseThrow(() ->
                new RuntimeException("Country not exist"));
        Car car = Car.builder()
                .name(name)
                .model(model)
                .year(year)
                .price(price)
                .country(country)
                .build();
        car.getCategories();
        Category category = new Category();
        category.getCars();
        carRepository.save(car);
        return "redirect:/cars";
    }

    @GetMapping("/get")
    public String getCar(@RequestParam(name = "id") Long id,
                         Model model) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car is not found"));
        model.addAttribute("car", car);
        List<Category> categories = categoryRepository.findAll();
        categories.removeAll(car.getCategories());
        model.addAttribute("categories", categories);
        return "car-details";
    }

    @GetMapping("/updatecar")
    public String editCar(@RequestParam(name = "id") Long id,
                          Model model) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car is not found"));
        model.addAttribute("car", car);
        return "edit-car";
    }

    @PostMapping("/updatecar")
    public String update(@RequestParam(name = "car_id") Long id,
                         @RequestParam(name = "car_name") String name,
                         @RequestParam(name = "car_model") String model,
                         @RequestParam(name = "car_year") Integer year,
                         @RequestParam(name = "car_price") Integer price) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car is not found"));
        car.setName(name);
        car.setYear(year);
        car.setPrice(price);
        car.setModel(model);
        carRepository.save(car);
        return "redirect:/cars";
    }

    @PostMapping("/deletecar")
    public String deleteCar(@RequestParam(name = "id") Long id) {
        carRepository.deleteById(id);
        return "redirect:/cars";
    }

    @PostMapping("/assigncategory")
    public String asssing(@RequestParam(name = "category_id") Long categoryId,
                          @RequestParam(name = "car_id") Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car is not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category is not found"));
        car.getCategories().add(category);
        carRepository.save(car);
        return "redirect:/cars";
    }

    @PostMapping("/unassign")
    public String unassign(@RequestParam(name = "category_id") Long categoryId,
                           @RequestParam(name = "car_id") Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car is not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category is not found"));
        car.getCategories().remove(category);
        carRepository.save(car);
        return "redirect:/cars";
    }


}
