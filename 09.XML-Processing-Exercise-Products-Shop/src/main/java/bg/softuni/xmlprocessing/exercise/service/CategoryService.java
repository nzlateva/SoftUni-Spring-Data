package bg.softuni.xmlprocessing.exercise.service;

import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_03.CategoryRootViewDto;
import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_03.CategoryViewDto;
import bg.softuni.xmlprocessing.exercise.model.dto.importdtos.CategorySeedDto;
import bg.softuni.xmlprocessing.exercise.model.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {

    void seedCategories(List<CategorySeedDto> categories) throws IOException;

    long getEntityCount();

    Set<Category> getRandomCategories();

    CategoryRootViewDto findAllByProductsCount();
}
