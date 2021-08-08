package bg.softuni.jsonprocessing.productshop.service.impl;

import bg.softuni.jsonprocessing.productshop.model.dto.exportdtos.CategoryViewDto;
import bg.softuni.jsonprocessing.productshop.model.dto.importdtos.CategorySeedDto;
import bg.softuni.jsonprocessing.productshop.model.entity.Category;
import bg.softuni.jsonprocessing.productshop.model.entity.Product;
import bg.softuni.jsonprocessing.productshop.repository.CategoryRepository;
import bg.softuni.jsonprocessing.productshop.service.CategoryService;
import bg.softuni.jsonprocessing.productshop.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static bg.softuni.jsonprocessing.productshop.constants.GlobalConstants.CATEGORIES_FILE_NAME;
import static bg.softuni.jsonprocessing.productshop.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() > 0) {
            return;
        }

        // read file
        String fileContent = Files
                .readString(Path.of(RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME));

        // deserialize string to array
        CategorySeedDto[] categorySeedDtos = this.gson
                .fromJson(fileContent, CategorySeedDto[].class);

        // validate, map to Category and save
        Arrays.stream(categorySeedDtos)
                .filter(this.validationUtil::isValid)
                .map(categorySeedDto -> this.modelMapper.map(categorySeedDto, Category.class))
                .forEach(this.categoryRepository::save);


//        Arrays.stream(
//                this.gson.fromJson(
//                        Files.readString(Path.of(RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME)),
//                        CategorySeedDto[].class
//                ))
//                .filter(this.validationUtil::isValid)
//                .map(categorySeedDto -> this.modelMapper.map(categorySeedDto, Category.class))
//                .forEach(this.categoryRepository::save);
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
    public List<CategoryViewDto> findAllByProductsCount() {
        return this.categoryRepository
                .findAllByProductsCount()
                .stream()
                .map(category -> {
                    CategoryViewDto categoryViewDto = this.modelMapper.map(category, CategoryViewDto.class);
                    categoryViewDto.setCategory(category.getName());
                    categoryViewDto.setProductsCount(category.getProducts().size());
                    categoryViewDto.setAveragePrice(getTotalRevenue(category)
                            .divide(BigDecimal.valueOf(category.getProducts().size()),
                                    6, RoundingMode.HALF_UP));
                    categoryViewDto.setTotalRevenue(getTotalRevenue(category));

                    return categoryViewDto;
                })
                .collect(Collectors.toList());

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
