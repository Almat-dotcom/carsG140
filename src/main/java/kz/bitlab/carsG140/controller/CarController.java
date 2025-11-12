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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
                      @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
                          Pageable pageable,
                      @RequestParam(value = "sort_by", required = false, defaultValue = "price") String sortBy,
                      @RequestParam(value = "sort_order", required = false, defaultValue = "ASC") String sortOrder,
                      Model model
    ) {

        Sort sort = Sort.by(sortOrder.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
//        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Specification<Car> specification = CarSpecification.getCarSpecification(name, minYear, maxPrice, countryId, categoryId);
        Page<Car> carsPage = carRepository.findAll(specification, pageable);

        List<Car> cars = carsPage.getContent();
        //количество
        //в какой странице ты

        model.addAttribute("cars", cars);
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentPage", carsPage.getNumber());
        model.addAttribute("totalPages", carsPage.getTotalPages());
        List<Integer> pages = IntStream.range(0, carsPage.getTotalPages()).boxed().toList();   // [0, 1, 2, …, N-1]
        model.addAttribute("pageNumbers", pages);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("pageSize", carsPage.getTotalPages());
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
