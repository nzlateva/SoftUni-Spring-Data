package bg.softuni.xmlprocessing.exercise.service.impl;

import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_03.CategoryRootViewDto;
import bg.softuni.xmlprocessing.exercise.model.dto.exportdtos.task_03.CategoryViewDto;
import bg.softuni.xmlprocessing.exercise.model.dto.importdtos.CategorySeedDto;
import bg.softuni.xmlprocessing.exercise.model.entity.Category;
import bg.softuni.xmlprocessing.exercise.model.entity.Product;
import bg.softuni.xmlprocessing.exercise.repository.CategoryRepository;
import bg.softuni.xmlprocessing.exercise.service.CategoryService;
import bg.softuni.xmlprocessing.exercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories(List<CategorySeedDto> categories) throws IOException {

        categories
                .stream()
                .filter(this.validationUtil::isValid)
                .map(categorySeedDto -> this.modelMapper.map(categorySeedDto, Category.class))
                .forEach(this.categoryRepository::save);

    }

    @Override
    public long getEntityCount() {
        return this.categoryRepository.count();
    }

    @Override
    public Set<Category> getRandomCategories() {

        int categoriesCount = ThreadLocalRandom
                .current()
                .nextInt(1, 4);

        long repoCount = this.categoryRepository.count();

        Set<Category> categories = new HashSet<>();

        for (int i = 0; i < categoriesCount; i++) {
            long id = ThreadLocalRandom
                    .current()
                    .nextLong(1, repoCount + 1);

            categories.add(this.categoryRepository.findById(id).orElse(null));
        }

        return categories;
    }

    @Override
    public CategoryRootViewDto findAllByProductsCount() {
        CategoryRootViewDto rootViewDto = new CategoryRootViewDto();
        rootViewDto.setCategories(this.categoryRepository
                .findAllByProductsCount()
                .stream()
                .map(category -> {
                    CategoryViewDto categoryViewDto = this.modelMapper.map(category, CategoryViewDto.class);
                    categoryViewDto.setProductsCount(category.getProducts().size());
                    categoryViewDto.setAveragePrice(getTotalRevenue(category)
                            .divide(BigDecimal.valueOf(category.getProducts().size()),
                                    6, RoundingMode.HALF_UP));
                    categoryViewDto.setTotalRevenue(getTotalRevenue(category));

                    return categoryViewDto;
                })
                .collect(Collectors.toList()));

        return rootViewDto;
    }

    private static BigDecimal getTotalRevenue(Category category) {
        return category
                .getProducts()
                .stream()
                .map(Product::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.valueOf(0));
    }
}
