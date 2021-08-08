package bg.softuni.jsonprocessing.exercise.service;

import bg.softuni.jsonprocessing.exercise.model.dto.exportdtos.CategoryViewDto;
import bg.softuni.jsonprocessing.exercise.model.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {

    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();

    List<CategoryViewDto> findAllByProductsCount();
}
