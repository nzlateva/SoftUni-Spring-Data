package bg.softuni.jsonprocessing.exercise.service.impl;

import bg.softuni.jsonprocessing.exercise.model.dto.exportdtos.ProductWithSellerDto;
import bg.softuni.jsonprocessing.exercise.model.dto.importdtos.ProductSeedDto;
import bg.softuni.jsonprocessing.exercise.model.entity.Product;
import bg.softuni.jsonprocessing.exercise.repository.ProductRepository;
import bg.softuni.jsonprocessing.exercise.service.CategoryService;
import bg.softuni.jsonprocessing.exercise.service.ProductService;
import bg.softuni.jsonprocessing.exercise.service.UserService;
import bg.softuni.jsonprocessing.exercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bg.softuni.jsonprocessing.exercise.constants.GlobalConstants.PRODUCTS_FILE_NAME;
import static bg.softuni.jsonprocessing.exercise.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedProducts() throws IOException {

        String fileContent =
                Files.readString(Path.of(RESOURCE_FILE_PATH + PRODUCTS_FILE_NAME));

        ProductSeedDto[] productSeedDtos = this.gson
                .fromJson(fileContent, ProductSeedDto[].class);

        Arrays.stream(productSeedDtos)
                .filter(this.validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = this.modelMapper.map(productSeedDto, Product.class);

                    product.setCategories(this.categoryService.getRandomCategories());

                    product.setSeller(this.userService.getRandomUser());

                    if (productSeedDto.getPrice().compareTo(new BigDecimal(700L)) > 0) {
                        product.setBuyer(this.userService.getRandomUser());
                    }

                    return product;
                })
                .forEach(this.productRepository::save);
    }

    @Override
    public List<ProductWithSellerDto> findProductsInRangeWithoutBuyer(BigDecimal lower, BigDecimal upper) {
        return this.productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPrice(lower, upper)
                .stream()
                .map(product -> this.modelMapper.map(product, ProductWithSellerDto.class))
                .collect(Collectors.toList());
    }
}
