package kz.bitlab.carsG140.service.impl;

import kz.bitlab.carsG140.entity.Car;
import kz.bitlab.carsG140.entity.Category;
import kz.bitlab.carsG140.repository.CategoryRepository;
import kz.bitlab.carsG140.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getNewCarCategories(Car car) {
        List<Category> categories = categoryRepository.findAll();
        categories.removeAll(car.getCategories());
        return categories;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(String categoryName) {
        Category category =
                Category.builder()
                        .name(categoryName)
                        .build();
        categoryRepository.save(category);
    }
}
