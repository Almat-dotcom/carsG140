package kz.bitlab.carsG140.controller;

import kz.bitlab.carsG140.entity.Category;
import kz.bitlab.carsG140.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping("/addcategory")
    public String addCategory() {
        return "add-category";
    }

    @PostMapping("/addcategory")
    public String addCategory(@RequestParam(name = "category_name") String categoryName) {
        Category category =
                Category.builder()
                        .name(categoryName)
                        .build();
        categoryRepository.save(category);
        return "redirect:/cars";
    }
}
