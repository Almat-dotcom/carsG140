package kz.bitlab.carsG140.controller;

import kz.bitlab.carsG140.entity.Car;
import kz.bitlab.carsG140.entity.Category;
import kz.bitlab.carsG140.service.CarService;
import kz.bitlab.carsG140.service.CategoryService;
import kz.bitlab.carsG140.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final CategoryService categoryService;
    private final CountryService countryService;

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
        Page<Car> carsPage = carService.findAll(name, minYear, maxPrice, categoryId, countryId, pageable);

        List<Car> cars = carsPage.getContent();
        List<Integer> pages = IntStream.range(0, carsPage.getTotalPages()).boxed().toList();   // [0, 1, 2, …, N-1]


        model.addAttribute("cars", cars);
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("currentPage", carsPage.getNumber());
        model.addAttribute("totalPages", carsPage.getTotalPages());
        model.addAttribute("pageNumbers", pages);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("pageSize", carsPage.getTotalPages());
        return "cars";
    }

    @GetMapping("/addcar")
    public String addCar(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "add-car";
    }

    @PostMapping("/addcar")
    public String addCarPost(@RequestParam(name = "car_name") String name,
                             @RequestParam(name = "car_model") String model,
                             @RequestParam(name = "car_year") Integer year,
                             @RequestParam(name = "car_price") Integer price,
                             @RequestParam(name = "country_id") Long countryId) {

        carService.addCar(name, model, year, price, countryId);
        return "redirect:/cars";
    }

    @GetMapping("/get")
    public String getCar(@RequestParam(name = "id") Long id,
                         Model model) {
        Car car = carService.findCarById(id);
        List<Category> categories = categoryService.getNewCarCategories(car);

        model.addAttribute("car", car);
        model.addAttribute("categories", categories);
        return "car-details";
    }

    @GetMapping("/updatecar")
    public String editCar(@RequestParam(name = "id") Long id,
                          Model model) {
        Car car = carService.findCarById(id);
        model.addAttribute("car", car);
        return "edit-car";
    }

    @PostMapping("/updatecar")
    public String update(@RequestParam(name = "car_id") Long id,
                         @RequestParam(name = "car_name") String name,
                         @RequestParam(name = "car_model") String model,
                         @RequestParam(name = "car_year") Integer year,
                         @RequestParam(name = "car_price") Integer price) {
        carService.updateCar(id, name, model, year, price);
        return "redirect:/cars";
    }

    @PostMapping("/deletecar")
    public String deleteCar(@RequestParam(name = "id") Long id) {
        carService.deleteCarById(id);
        return "redirect:/cars";
    }

    @PostMapping("/assigncategory")
    public String asssing(@RequestParam(name = "category_id") Long categoryId,
                          @RequestParam(name = "car_id") Long carId) {
        carService.assignCategory(categoryId, carId);
        return "redirect:/cars";
    }

    @PostMapping("/unassign")
    public String unassign(@RequestParam(name = "category_id") Long categoryId,
                           @RequestParam(name = "car_id") Long carId) {
        carService.unassignCategory(categoryId, carId);
        return "redirect:/cars";
    }


}
