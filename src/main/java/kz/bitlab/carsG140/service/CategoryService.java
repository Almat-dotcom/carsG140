package kz.bitlab.carsG140.service;

import kz.bitlab.carsG140.entity.Car;
import kz.bitlab.carsG140.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getNewCarCategories(Car car);

    List<Category> findAll();

    void addCategory(String categoryName);
}
